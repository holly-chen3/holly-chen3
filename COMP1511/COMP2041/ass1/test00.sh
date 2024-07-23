#!/bin/dash
# Testing correct implementation of pushy-init

PATH="$PATH:$(pwd)"
INDEX=".pushy"

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
# expected: a .pushy folder exists
rm -r $INDEX 2>/dev/null
cat > "$expected_output" <<EOF
Initialized empty pushy repository in .pushy
EOF
pushy-init > "$actual_output" 2>&1
comp 1

### TEST 02 - test folder already exists ###
# expected: a .pushy folder already exists, print error
cat > "$expected_output" <<EOF
pushy-init: error: .pushy already exists
EOF
pushy-init > "$actual_output" 2>&1
comp 2
echo "Finished successfully!"
