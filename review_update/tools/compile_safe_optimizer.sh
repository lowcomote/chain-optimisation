#!/bin/sh
set -eu
# Compile the review-safe source standalone, without Eclipse dependency resolution.
cd "$(dirname "$0")/../.."
mkdir -p /tmp/chain-review-safe-classes
javac --release 11 -d /tmp/chain-review-safe-classes org.eclipse.epsilon.etl.chain.optimisation/src/org/eclipse/epsilon/etl/chain/optimisation/SafeEtlChainOptimizer.java
echo "Compiled SafeEtlChainOptimizer.java (Java 11 target)."
echo "Real ETL output comparisons must be run in Eclipse Epsilon with the original benchmark."
