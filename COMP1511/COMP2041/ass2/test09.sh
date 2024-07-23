#!/bin/dash
# Testing correct implementation of subset 1: comments and whitespace

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
# expected: whitespaces are ignored
cat > "$expected_output" <<EOF
9
10
11
11
13
13
14
EOF
seq 9 15 | eddy.py '/4/q; /2/ s/2/1/; /3/  p' > "$actual_output" 2>&1
comp 1

### TEST 02 - test index case ###
# expected: works for index addresses
cat > "$expected_output" <<EOF
1
3
4
5
6
7
EOF
seq 1 7 > first.txt |
eddy.py '2   d;   2 p' first.txt > "$actual_output" 2>&1
comp 2

### TEST 03 - test comments in -f file ###
# expected: comments are removed
cat > "$expected_output" <<EOF
9
10
10
11
11
13
14
EOF
seq 9 10 > first.txt |
seq 10 15 > second.txt |
echo '/4/ q #  haha ; /2/s/2/1/  # heheee' > commands.eddy |
echo '  /3/ p' >> commands.eddy |
eddy.py -f commands.eddy first.txt second.txt > "$actual_output" 2>&1
comp 3

### TEST 04 - test comments outside of files ###
# expected: comments are removed
cat > "$expected_output" <<EOF
Hallo
Hallo
Holly
EOF
echo Hallo > first.txt |
echo Holly > second.txt |
echo '/y/q;/a/p' > commands.eddy |
eddy.py '   /y/ q # idk;        /a/ p #maybee' first.txt second.txt > "$actual_output" 2>&1
comp 4
echo "Finished successfully!"