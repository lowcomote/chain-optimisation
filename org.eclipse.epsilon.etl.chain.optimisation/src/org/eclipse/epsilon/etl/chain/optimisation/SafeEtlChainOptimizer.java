package org.eclipse.epsilon.etl.chain.optimisation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 * Review-driven, conservative alternative to the legacy index-based ETL rewriter.
 * This intentionally supports ONLY fully analyzable, batch, unidirectional ETL:
 * exactly two EMF models, rules with one source and one-or-more targets,
 * primitive attribute-to-attribute/literal bindings, no guards, pre/post,
 * operations, annotations, inheritance, references, tracing, or side effects.
 *
 * If ANY step cannot be proved safe, the ENTIRE CHAIN is copied unchanged.
 * The original scripts and experimental inputs are never modified.
 * This is not a substitute for EMF Compare validation of actual outputs.
 *
 * Usage: java ...SafeEtlChainOptimizer --metamodels DIR --out DIR A.etl B.etl [...]
 */
public final class SafeEtlChainOptimizer {
    private SafeEtlChainOptimizer() {}

    private static final Pattern MODEL = Pattern.compile(
        "\\bmodel\\s+(\\w+)\\s+driver\\s+EMF\\s*\\{\\s*nsuri\\s*=\\s*\"([^\"]+)\"\\s*;?\\s*}\\s*;?",
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern RULE_HEADER = Pattern.compile(
        "\\brule\\s+(\\w+)\\s+transform\\s+(\\w+)\\s*:\\s*(\\w+)!(\\w+)\\s+to\\s+" +
        "((?:\\w+\\s*:\\s*\\w+!\\w+\\s*,?\\s*)+)\\{",
        Pattern.DOTALL);
    private static final Pattern TARGET = Pattern.compile("(\\w+)\\s*:\\s*(\\w+)!(\\w+)");
    private static final Pattern BINDING = Pattern.compile(
        "\\s*(\\w+)\\.(\\w+)\\s*=\\s*(?:(\\w+)\\.(\\w+)|'[^'\\n]*'|\"[^\"\\n]*\"|[+-]?[0-9]+(?:\\.[0-9]+)?|true|false)\\s*;\\s*",
        Pattern.DOTALL);
    private static final Pattern UNSAFE = Pattern.compile(
        "\\b(?:pre|post|operation|import|extends|guard|execute|if|else|for|while|var|new|do|return|" +
        "switch|equivalent|equivalents|allInstances|all|select|collect|flatten|first|asSequence|println|" +
        "delete|transformation|migrate|greedy|extends|abstract|lazy)\\b|@",
        Pattern.CASE_INSENSITIVE);

    private static final class Type {
        final String model, name;
        Type(String model, String name) { this.model = model; this.name = name; }
    }
    private static final class Rule {
        final String name; final int start, end; final Type source;
        final List<Type> targets;
        Rule(String n, int a, int b, Type s, List<Type> t) {
            name=n; start=a; end=b; source=s; targets=t;
        }
    }
    private static final class Stage {
        final Path path; final String content;
        final Map<String,String> modelUris = new LinkedHashMap<>();
        final List<Rule> rules = new ArrayList<>();
        Stage(Path p, String c) { path=p; content=c; }
        String inputUri() { return modelUris.get(rules.get(0).source.model); }
        String outputUri() { return modelUris.get(rules.get(0).targets.get(0).model); }
    }
    private static final class Meta {
        final String uri; final Map<String,Set<String>> attributes = new HashMap<>();
        boolean hasReferencesOrInheritance=false;
        Meta(String u) { uri=u; }
        boolean has(String cls, String feature) {
            return attributes.containsKey(cls) && attributes.get(cls).contains(feature);
        }
    }
    private static final class Problem extends Exception {
        Problem(String text) { super(text); }
    }
    private static String eraseComments(String text) throws Problem {
        // Preserve length/positions: comments are replaced with spaces.
        StringBuilder b = new StringBuilder(text);
        boolean line=false, block=false; char quote=0;
        for(int i=0;i<text.length();i++) {
            char c=text.charAt(i), n=(i+1<text.length()?text.charAt(i+1):0);
            if(line){ if(c=='\n'||c=='\r') line=false; else b.setCharAt(i,' '); continue; }
            if(block){ b.setCharAt(i,' '); if(c=='*'&&n=='/'){b.setCharAt(i+1,' ');i++;block=false;} continue; }
            if(quote!=0){if(c=='\\'&&i+1<text.length()){i++;continue;}if(c==quote)quote=0;continue;}
            if(c=='\''||c=='\"'){quote=c;continue;}
            if(c=='/'&&n=='/'){b.setCharAt(i,' ');b.setCharAt(i+1,' ');i++;line=true;continue;}
            if(c=='/'&&n=='*'){b.setCharAt(i,' ');b.setCharAt(i+1,' ');i++;block=true;continue;}
        }
        if(block||quote!=0)throw new Problem("unterminated comment or string");
        return b.toString();
    }
    private static int matchingBrace(String s, int open) throws Problem {
        int depth=0; char quoted=0;
        for(int i=open;i<s.length();i++) {
            char ch=s.charAt(i);
            if(quoted!=0) {if(ch=='\\')i++;else if(ch==quoted)quoted=0;continue;}
            if(ch=='\''||ch=='\"'){quoted=ch;continue;}
            if(ch=='{')depth++;
            if(ch=='}'&&--depth==0)return i+1;
        }
        throw new Problem("unbalanced braces");
    }
    private static Stage parse(Path file) throws IOException,Problem {
        String content=Files.readString(file,StandardCharsets.UTF_8);
        String stripped=eraseComments(content);
        // Require that the only top-level constructs are exactly 2 model declarations and rules.
        StringBuilder rest=new StringBuilder(stripped);
        Stage st=new Stage(file,content);
        Matcher models=MODEL.matcher(stripped);
        while(models.find()){
            if(st.modelUris.put(models.group(1),models.group(2))!=null)throw new Problem("duplicate model declaration");
            rest.replace(models.start(),models.end()," ".repeat(models.end()-models.start()));
        }
        if(st.modelUris.size()!=2)throw new Problem("only two EMF models per stage are supported");
        Matcher matcher=RULE_HEADER.matcher(stripped);
        while(matcher.find()) {
            final int end=matchingBrace(stripped,matcher.end()-1);
            String body=stripped.substring(matcher.end(),end-1);
            if(UNSAFE.matcher(body).find()) throw new Problem("imperative/guard/trace construct in rule "+matcher.group(1));
            String srcModel=matcher.group(3),srcClass=matcher.group(4),srcName=matcher.group(2);
            List<Type> targets=new ArrayList<>();Set<String> targetVars=new HashSet<>();
            Matcher tm=TARGET.matcher(matcher.group(5));
            while(tm.find()){
                targets.add(new Type(tm.group(2),tm.group(3)));
                if(!targetVars.add(tm.group(1)))throw new Problem("duplicate target variable");
            }
            String noTargets=TARGET.matcher(matcher.group(5)).replaceAll("").replace(",","").trim();
            if(!noTargets.isEmpty()||targets.isEmpty())throw new Problem("invalid target parameter list");
            if(st.modelUris.containsKey(srcModel)==false)throw new Problem("unknown source model "+srcModel);
            for(Type t:targets) if(!st.modelUris.containsKey(t.model) || t.model.equals(srcModel))
                throw new Problem("unsupported target model for "+matcher.group(1));
            String remaining=body;
            Matcher bm=BINDING.matcher(body);
            int bound=0;
            while(bm.find()){
                String lhsVar=bm.group(1);
                if(!targetVars.contains(lhsVar))throw new Problem("binding to non-target variable "+lhsVar);
                if(bm.group(3)!=null&&!srcName.equals(bm.group(3)))
                    throw new Problem("binding reads another variable "+bm.group(3));
                bound++;
                // Validate attribute feature against Ecore later.
                remaining=remaining.substring(0,bm.start()) + " ".repeat(bm.end()-bm.start()) + remaining.substring(bm.end());
            }
            if(!remaining.trim().isEmpty())throw new Problem("unrecognized rule body "+matcher.group(1));
            st.rules.add(new Rule(matcher.group(1),matcher.start(),end,new Type(srcModel,srcClass),targets));
            rest.replace(matcher.start(),end," ".repeat(end-matcher.start()));
        }
        if(st.rules.isEmpty()||!rest.toString().trim().isEmpty())
            throw new Problem("unsupported non-rule syntax, annotations, globals, pre/post, or operations");
        // All rules of a stage must use precisely the same input and output models.
        String si=st.rules.get(0).source.model,so=st.rules.get(0).targets.get(0).model;
        for(Rule r:st.rules){
            if(!si.equals(r.source.model))throw new Problem("multiple source models");
            for(Type t:r.targets)if(!so.equals(t.model))throw new Problem("multiple target models");
        }
        return st;
    }
    private static Map<String,Meta> loadMetamodels(Path dir) throws Exception {
        Map<String,Meta> metas=new HashMap<>();
        if(!Files.isDirectory(dir))throw new Problem("metamodel directory not found: "+dir);
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities",false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities",false);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING,true);
        factory.setXIncludeAware(false);factory.setExpandEntityReferences(false);
        try(DirectoryStream<Path> ds=Files.newDirectoryStream(dir,"*.ecore")) {
            for(Path p:ds){
                Document doc=factory.newDocumentBuilder().parse(p.toFile());
                Element root=doc.getDocumentElement();
                String uri=root.getAttribute("nsURI");
                if(uri.isBlank())continue;
                Meta meta=new Meta(uri);
                NodeList all=doc.getElementsByTagName("eClassifiers");
                for(int i=0;i<all.getLength();i++){
                    Element cls=(Element)all.item(i);
                    String type=cls.getAttribute("xsi:type");
                    if(!type.endsWith("EClass"))continue;
                    String name=cls.getAttribute("name");
                    if(meta.attributes.putIfAbsent(name,new LinkedHashSet<>())!=null)throw new Problem("duplicate class " +name);
                    if(cls.hasAttribute("eSuperTypes"))meta.hasReferencesOrInheritance=true;
                    NodeList children=cls.getChildNodes();
                    for(int k=0;k<children.getLength();k++){
                        Node node=children.item(k);
                        if(!(node instanceof Element))continue;
                        Element e=(Element)node;
                        if(!"eStructuralFeatures".equals(e.getNodeName()))continue;
                        String ft=e.getAttribute("xsi:type");
                        if(ft.endsWith("EReference"))meta.hasReferencesOrInheritance=true;
                        else if(ft.endsWith("EAttribute"))meta.attributes.get(name).add(e.getAttribute("name"));
                        else throw new Problem("unrecognized structural feature type in "+name);
                    }
                }
                // Different .ecore files with same nsURI are not unambiguously resolvable.
                if(metas.putIfAbsent(uri,meta)!=null)throw new Problem("duplicate metamodel nsURI: "+uri);
            }
        }
        return metas;
    }
    private static void validate(Stage st,Map<String,Meta> metas) throws Problem {
        Meta src=metas.get(st.inputUri()),dst=metas.get(st.outputUri());
        if(src==null||dst==null)throw new Problem("missing Ecore metamodel for "+st.path.getFileName());
        if(src.hasReferencesOrInheritance||dst.hasReferencesOrInheritance)
            throw new Problem("references or inheritance require richer static analysis: "+st.path.getFileName());
        for(Rule r:st.rules) {
            if(!src.attributes.containsKey(r.source.name))throw new Problem("unknown source class "+r.source.name);
            for(Type t:r.targets)if(!dst.attributes.containsKey(t.name))throw new Problem("unknown target class "+t.name);
        }
        // We additionally verify every assignment references valid attributes.
        String stripped=eraseComments(st.content);
        for(Rule rule:st.rules){
            String full=stripped.substring(rule.start,rule.end);
            Matcher hm=RULE_HEADER.matcher(full);
            if(!hm.find())throw new Problem("rule header changed unexpectedly");
            String body=full.substring(hm.end(),full.length()-1);
            Matcher bm=BINDING.matcher(body);
            Matcher vars=TARGET.matcher(hm.group(5));
            Map<String,String> targetNames=new HashMap<>();
            while(vars.find())targetNames.put(vars.group(1),vars.group(3));
            while(bm.find()){
                String targetClass=targetNames.get(bm.group(1));
                if(!dst.has(targetClass,bm.group(2)))throw new Problem("non-attribute target binding "+bm.group(1)+"."+bm.group(2));
                if(bm.group(4)!=null&&!src.has(rule.source.name,bm.group(4)))
                    throw new Problem("non-attribute source read "+bm.group(3)+"."+bm.group(4));
            }
        }
    }
    public static void main(String[] args) throws Exception {
        if(args.length<6 || !"--metamodels".equals(args[0]) || !"--out".equals(args[2])) {
            System.err.println("Usage: SafeEtlChainOptimizer --metamodels DIR --out DIR first.etl second.etl [...]");
            System.exit(2);
        }
        Path metaDir=Path.of(args[1]),out=Path.of(args[3]);
        List<Path> inputs=new ArrayList<>();
        for(int i=4;i<args.length;i++)inputs.add(Path.of(args[i]));
        if(inputs.size()<2)throw new IllegalArgumentException("At least two stages required");
        Files.createDirectories(out);
        Set<Path> reservedOutputs = new HashSet<>();
        Set<Path> absoluteInputs = new HashSet<>();
        for(Path input : inputs) absoluteInputs.add(input.toAbsolutePath().normalize());
        for(Path input : inputs) {
            Path target = out.resolve(input.getFileName()).toAbsolutePath().normalize();
            if (absoluteInputs.contains(target))
                throw new IllegalArgumentException("Output would overwrite an input: " + target);
            if (!reservedOutputs.add(target))
                throw new IllegalArgumentException("Duplicate output basename: " + target);
        }
        List<Stage> stages=new ArrayList<>();
        List<Set<String>> kept=new ArrayList<>();
        String failure=null;
        try {
            Map<String,Meta> metas=loadMetamodels(metaDir);
            for(Path p:inputs)stages.add(parse(p));
            for(Stage st:stages)validate(st,metas);
            for(int i=0;i<stages.size()-1;i++) {
                if(!stages.get(i).outputUri().equals(stages.get(i+1).inputUri()))
                    throw new Problem("adjacent metamodel mismatch at stage "+(i+1));
            }
            // Backward liveness, class-qualified by the shared Ecore nsURI.
            Set<String> demand=new LinkedHashSet<>();
            for(Rule r:stages.get(stages.size()-1).rules)
                for(Type t:r.targets)demand.add(t.name);
            for(int i=stages.size()-1;i>=0;i--){
                Stage st=stages.get(i);Set<String> keep=new LinkedHashSet<>();Set<String> sources=new LinkedHashSet<>();
                for(Rule r:st.rules){
                    boolean wanted=false;
                    for(Type t:r.targets)if(demand.contains(t.name))wanted=true;
                    if(wanted){keep.add(r.name);sources.add(r.source.name);}
                }
                kept.add(0,keep);demand=sources;
            }
        } catch(Exception ex){failure=ex.getMessage();}
        List<String> report=new ArrayList<>();
        report.add("status\t"+(failure==null?"SUPPORTED_SUBSET":"UNCHANGED_UNSUPPORTED"));
        report.add("reason\t"+(failure==null?"Pure attribute-only chain; conservative backward class liveness":failure));
        for(int i=0;i<inputs.size();i++) {
            Path input=inputs.get(i),dst=out.resolve(input.getFileName());
            if(input.toAbsolutePath().normalize().equals(dst.toAbsolutePath().normalize()))
                throw new IllegalArgumentException("Output may not overwrite input: "+input);
            String content=Files.readString(input,StandardCharsets.UTF_8);
            List<String> removed=new ArrayList<>();
            if(failure==null){
                Stage st=stages.get(i);StringBuilder build=new StringBuilder(content);
                for(int k=st.rules.size()-1;k>=0;k--){
                    Rule rule=st.rules.get(k);
                    if(!kept.get(i).contains(rule.name)){
                        build.delete(rule.start,rule.end);removed.add(rule.name);
                    }
                }
                content=build.toString();
                Collections.reverse(removed);
            }
            Files.writeString(dst,content,StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
            report.add("stage\t"+(i+1)+"\t"+input.getFileName()+"\tremoved="+removed);
        }
        Files.write(out.resolve("optimization_report.tsv"),report,StandardCharsets.UTF_8);
        report.forEach(System.out::println);
        if(failure!=null)System.out.println("No optimization applied: input scripts copied byte-for-byte.");
        else System.out.println("Optimizer output is UNVALIDATED until native ETL + EMF Compare checks pass.");
    }
}