# JOT reviewer comments mapped to code changes

This review matrix is grounded in the supplied `Gmail - [JOT] Editor Decision.pdf` and public GitHub repository `lowcomote/chain-optimisation` tree `987f48016221ce5fca78f0e497e3dbeeb3f94e16`.

| Reviewer concern | What was done | Status and evidence |
|---|---|---|
| B: Algorithm 2's `SPs == TPs` mapping is unreliable | Removed equality-of-type logic from the new opt-in optimizer. It instead propagates a demand set *backwards* through complete, typed 1:n pure rules and Ecore classes | Implemented for restricted subset; 18 tests |
| B: multiple output parameters per rule (1:n) | Keep a rule if any of its target classes is demanded; do not prune one target parameter in isolation | Implemented and tested |
| B: n:1 aggregation and complex expressions | Reject as unsupported; retain the whole original chain | Safe fallback; not optimized |
| B: purpose of rule dependency graph | Backward demanded classes represent inter-stage data flow. Explicit trace, guard, reference and statement dependencies need a richer ETL AST frontend; old dependency graph cannot be interpreted as chain graph | Explained; implementation incomplete for general ETL |
| B: errors from deleting statements/rules by positional index | New source transformer uses exact rule spans, works from end to beginning, does not delete bindings or useful empty creators; adds a safety gate to old destructive menu options | Implemented and tested for simple rules |
| B: assumptions/type of chain | Supports only sequential, batch, unidirectional, pure attribute-only ETL; one input model/one output model per stage, one source and 1:n outputs per rule | Enforced by parser and Ecore validation |
| B/D: strong threats to external validity | Preserve all existing benchmark data; the real repository scripts are complex and are not optimized by this restricted safe mode | Explicit limitation |
| D: memory consumption and optimizer overhead | Documentation clarifies generated element count != RAM use and requests separate preprocessing/memory recordings | Protocol only: no new measurements |
| D: EMF Compare/model equivalence | Added an explicit native ETL validation protocol and output comparison requirements | Not executed; cannot claim zero differences |
| D: aggregate runtime/variance and larger cases | Procedure specifies ten individual times, median/IQR, project/environment details and limitations | Protocol only; historical benchmark unchanged |
| D: alternatives/language portability | Core dependency model separate from ETL interface; ATL support would require its own validated frontend | Not implemented |

## Repository audit observations

`EtlChainOptimiser.java` contains a positional deletion (`ruleblock.getStatements().remove(index)`) and deletes a rule based on the number of remaining statements, not full trace/side-effect semantics. The GitHub benchmark ETL scripts include `pre`/`post` hooks, `equivalent()`/`equivalents()`, @lazy rules, inheritance, multi-target output, guards and imperative code. Consequently, deleting operations merely because adjacent class names do not match cannot establish semantic preservation. The package intentionally does not claim to address these constructs through type-only matching.

## Test scope, as of 10 October 2026

- 18 unit/CLI/patch-installer tests pass under OpenJDK 21.
- The standalone new class compiles without Eclipse dependencies.
- The sample 2-step ETL code is transformed and retains the last-stage script byte-for-byte.
- The patch installer compiles in a minimal synthetic entry-point smoke test.
- The original Eclipse/Epsilon project has **not** been compiled or executed here; the old environment and native project dependencies are not present.
- No GitHub commit or push has been performed.

**A revised publication cannot claim that this code has reproduced the previous benchmark speedups or established semantic equivalence for arbitrary ETL chains.**