#!/bin/dash
# Testing correct implementation of subset 1: addresses

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

### TEST 01 - test $q case ###
# expected: $ works for q
cat > "$expected_output" <<EOF
1
2
3
4
5
EOF
seq 1 5 | eddy.py '$q' > "$actual_output" 2>&1
comp 1

### TEST 02 - test $d case ###
# expected: $ works for d
cat > "$expected_output" <<EOF
1
2
3
4
EOF
seq 1 5 | eddy.py '$d' > "$actual_output" 2>&1
comp 2

### TEST 03 - test $p case ###
# expected: $ works for p
cat > "$expected_output" <<EOF
1
2
3
4
5
5
EOF
seq 1 5 | eddy.py '$p' > "$actual_output" 2>&1
comp 3

### TEST 04 - test $s case ###
# expected: $ works for s
cat > "$expected_output" <<EOF
1
2
3
4
hehe
EOF
seq 1 5 | eddy.py '$s/./hehe/' > "$actual_output" 2>&1
comp 4

### TEST 05 - test comma index addresses ###
# expected: comma separation works
cat > "$expected_output" <<EOF
1
2
hehe
hehe
hehe
EOF
seq 1 5 | eddy.py '3,5s/./hehe/' > "$actual_output" 2>&1
comp 5

### TEST 05 - test comma regex addresses ###
# expected: comma separation works for regex
cat > "$expected_output" <<EOF
hehe
hehe
hehe
4
5
EOF
seq 1 5 | eddy.py '/1/,/3/s/./hehe/' > "$actual_output" 2>&1
comp 6

### TEST 07 - test comma regex addresses for print ###
# expected: comma separation works for regex print
cat > "$expected_output" <<EOF
1
1
2
2
3
3
4
5
EOF
seq 1 5 | eddy.py '/1/,/3/p' > "$actual_output" 2>&1
comp 7

### TEST 08 - test comma regex addresses for delete ###
# expected: comma separation works for regex delete
cat > "$expected_output" <<EOF
4
5
EOF
seq 1 5 | eddy.py '/1/,/3/d' > "$actual_output" 2>&1
comp 8

### TEST 09 - test comma regex addresses for multiple commands ###
# expected: comma separation works for regex multiple commands
cat > "$expected_output" <<EOF
4
4
5
5
EOF
seq 1 5 | eddy.py '/1/,/3/d; /4/,/5/p' > "$actual_output" 2>&1
comp 9

### TEST 10 - test comma regex and index addresses ###
# expected: comma separation works for regex and index
cat > "$expected_output" <<EOF
4
4
5
5
EOF
seq 1 5 | eddy.py '/1/,3d; 4,/5/p' > "$actual_output" 2>&1
comp 10
echo "Finished successfully!"