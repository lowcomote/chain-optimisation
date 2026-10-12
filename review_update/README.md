# Review-driven code update: model transformation chain optimizer

Source audited: [lowcomote/chain-optimisation](https://github.com/lowcomote/chain-optimisation), branch `main`, Git tree `987f48016221ce5fca78f0e497e3dbeeb3f94e16` (checked 10 October 2026). Review basis: *[JOT] Editor Decision*, Reviewers B and D.

## What changed

1. `SafeEtlChainOptimizer.java` implements a *new, opt-in, fail-closed* source transformer rather than deleting ETL statements by position. It computes a backward class-demand set for the entire chain. It retains a 1:n creation rule if **any** target class is required; this is deliberately conservative.
2. A strict Ecore-aware parser accepts **only** a proven pure subset of ETL: two declared EMF models per stage, explicit rule source and target types, simple attribute-to-attribute/constant assignments, no references/inheritance, no trace resolution, guards, operations, `pre`/`post`, dynamic queries, annotations, imperative constructs, or other unrecognized syntax. This restriction is essential because the available implementation does not perform whole-ETL side-effect and alias analysis. **Anything unsupported causes the entire chain to be copied unchanged**; a reason is recorded in `optimization_report.tsv`.
3. Existing transformation files, datasets, figures, evaluation tables, and benchmark parameters are untouched. Outputs go to another folder; the tool refuses to overwrite inputs.
4. `tools/apply_to_github_checkout.py` installs the new class in a local clone and patches `EtlChainOptimiser.main()` to offer an explicit `--safe-optimize` entry point. It also blocks the original destructive optimization menu actions by default. A property flag can temporarily re-enable them for reproducing historical runs; do not cite them as validated.
5. The package includes a dependency-liveness reference model (`BackwardChainOptimizer.java`), 18 automated Java/CLI and patch-integration tests, and the input/output equivalence protocol.

## Important scope limitation

The real KM3 → Ecore → JavaSource → Table → HTML → XML transformation case uses `equivalent()`, lazy rules, inheritance, cross-references, global operations and `pre`/`post`. **This specific dataset is currently classified as unsupported by the safe subset and is copied without rewriting.** The code therefore prevents an unsound optimization but **does not reproduce the earlier paper's speedups** and is **not an end-to-end fix** for the published Eclipse implementation. Fully optimizing that example needs an ETL AST/data-flow frontend, trace analysis, effect analysis, and native EMF Compare validation. These have not been claimed or simulated.

## Apply the patch to your checkout

```bash
git clone https://github.com/lowcomote/chain-optimisation.git
python tools/apply_to_github_checkout.py ./chain-optimisation --dry-run
python tools/apply_to_github_checkout.py ./chain-optimisation
```

`apply_to_github_checkout.py` is shipped inside this package. The original Java entry point is backed up as `EtlChainOptimiser.java.pre-review-backup`. This patch does **not** push a commit to GitHub. Inspect and commit the changes yourself.

## Compile/run without Eclipse for the pure subset

Requires Java 11+ (locally tested using Java 21):

```bash
javac -d build src/org/eclipse/epsilon/etl/chain/optimisation/SafeEtlChainOptimizer.java
java -cp build org.eclipse.epsilon.etl.chain.optimisation.SafeEtlChainOptimizer \
  --metamodels fixtures/mm --out fixtures/optimized \
  fixtures/Person2Messenger.etl fixtures/Messenger2Network.etl
```

The two-stage demonstration should report `removed=[Person2Account]` and preserve `Device2EnabledDevice` and `Enabled2Network`. It is a controlled pure-attribute example, not a replacement for the original paper's benchmark.

Inside the patched Eclipse Java project:

```text
EtlChainOptimiser --safe-optimize --metamodels metamodels --out optimized-safe \
  scripts/Stage1.etl scripts/Stage2.etl ...
```

For a published benchmark chain with unsupported constructs, the copied scripts are unchanged. Do not infer speedups from those outputs.

## Run tests

```bash
python3 tests/test_safe_optimizer.py
```

18 checks include creation without deleting necessary 1:n rules, independent dead rule removal, preservation of all input bytes, conservative fallback for `equivalent()` and guards, invalid metamodels, references, inheritance, unknown bindings, unsupported loops, output-path protection, and a compiled installation-patch smoke test. These are **unit/CLI tests**, not native ETL model-equivalence tests.

## Required before a journal-level correctness statement

Follow `NATIVE_ETL_VALIDATION.md` with the original Eclipse/Epsilon stack. Capture for each of the original and optimized chain outputs: a reproducible run identifier, metamodel versions, EMF Compare diff count, and ordered-containment options. All 32 model comparisons (16 per chain, if those were indeed the experimental inputs) must be evidenced; model equality cannot be inferred from type compatibility. Include per-run durations, warmups, model size, and optimization preprocessing cost; do not invent missing historical data. A second independent case study and a separate language backend would be future experiments, not features represented here.