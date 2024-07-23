#!/bin/dash
# Testing correct implementation of subset 0: s command

PATH="$PATH:$(pwd)"
trap 'rm "$expected_output" "$actual_output" -rf "$testingDir"' INT HUP QUIT TERM EXIT

testingDir="$(mktemp -d)"
cd "$testingDir" || exit 1

expected_output="$(mktemp)" 
actual_output="$(mktemp)" 
# HELPER FUNCTION - comparing the actual output to expected output
comp() {
	if diff "$expected_output" "$actual_output" >/dev/null
	then
	   echo "Test $1 passed!"
	else
	   echo "Test $1 failed!"
	fi
	echo '\n'
}
### TEST 01 - test base case ###
# expected: substitutes the specified regex
cat > "$expected_output" <<EOF
1
2
3
4
zzz
EOF
seq 1 5 | eddy.py 's/5/zzz/' > "$actual_output" 2>&1
comp 1

### TEST 02 - test global case ###
# expected: substitutes every specified regex
cat > "$expected_output" <<EOF
9
hi0
hihi
hi2
hi3
hi4
EOF
seq 9 14 | eddy.py 's/1/hi/g' > "$actual_output" 2>&1
comp 2

### TEST 03 - test regex for non digits ###
# expected: substitutes non digits
cat > "$expected_output" <<EOF
Hillo Hillo
EOF
echo Hello Hullo | eddy.py 's/[eu]/i/g' > "$actual_output" 2>&1
comp 3

### TEST 04 - test index address ###
# expected: substitutes at the particular index
cat > "$expected_output" <<EOF
12
13
hi4
15
16
EOF
seq 12 16 | eddy.py '3s/1/hi/g' > "$actual_output" 2>&1
comp 4
echo "Finished successfully!"