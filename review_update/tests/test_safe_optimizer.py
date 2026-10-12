#!/usr/bin/env python3
"""Local smoke tests for the restricted safe optimizer (NOT native ETL execution)."""
import subprocess
import tempfile
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
SRC = ROOT / "org.eclipse.epsilon.etl.chain.optimisation/src/org/eclipse/epsilon/etl/chain/optimisation/SafeEtlChainOptimizer.java"
FIX = ROOT / "review_update/fixtures"
MAIN = "org.eclipse.epsilon.etl.chain.optimisation.SafeEtlChainOptimizer"

class SafeOptimizerSmokeTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.classes = tempfile.TemporaryDirectory(prefix="safe-etl-java-")
        cp = subprocess.run(["javac", "--release", "11", "-d", cls.classes.name, str(SRC)],
                            text=True, capture_output=True)
        assert cp.returncode == 0, cp.stderr

    @classmethod
    def tearDownClass(cls):
        cls.classes.cleanup()

    def execute(self, first=None):
        with tempfile.TemporaryDirectory(prefix="safe-etl-fixture-") as tmp:
            tmp = Path(tmp)
            original = (FIX / "Person2Messenger.etl").read_text()
            (tmp / "Person2Messenger.etl").write_text(first or original)
            (tmp / "Messenger2Network.etl").write_text((FIX / "Messenger2Network.etl").read_text())
            output = tmp / "out"
            cmd = ["java", "-cp", self.classes.name, MAIN, "--metamodels",
                   str(FIX / "mm"), "--out", str(output), str(tmp / "Person2Messenger.etl"),
                   str(tmp / "Messenger2Network.etl")]
            cp = subprocess.run(cmd, text=True, capture_output=True)
            self.assertEqual(cp.returncode, 0, cp.stderr)
            return cp.stdout, (output / "Person2Messenger.etl").read_text(), \
                   (output / "Messenger2Network.etl").read_text(), \
                   original, (tmp / "Person2Messenger.etl").read_text()

    def test_remove_dead_account_rule(self):
        log, first, second, _, _ = self.execute()
        self.assertIn("SUPPORTED_SUBSET", log)
        self.assertNotIn("rule Person2Account", first)
        self.assertIn("rule Device2EnabledDevice", first)
        self.assertIn("rule Enabled2Network", second)

    def test_inputs_not_changed(self):
        _, _, _, original, source = self.execute()
        self.assertEqual(source, original)

    def test_equivalent_trace_refuses_to_modify_chain(self):
        source = (FIX / "Person2Messenger.etl").read_text().replace(
            "a.login = p.name;", "a.login = p.name.equivalent();")
        log, first, _, _, _ = self.execute(source)
        self.assertIn("UNCHANGED_UNSUPPORTED", log)
        self.assertEqual(first, source)

    def test_guard_refuses_to_modify_chain(self):
        source = (FIX / "Person2Messenger.etl").read_text().replace(
            "a.login = p.name;", "guard: p.name.isDefined() a.login = p.name;")
        log, first, _, _, _ = self.execute(source)
        self.assertIn("UNCHANGED_UNSUPPORTED", log)
        self.assertEqual(first, source)

if __name__ == "__main__":
    unittest.main(verbosity=2)
