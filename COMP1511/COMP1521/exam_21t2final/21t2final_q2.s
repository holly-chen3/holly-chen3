# COMP1521 21T2 ... final exam, question 2

	.data
even_parity_str:	.asciiz "the parity is even\n"
odd_parity_str:		.asciiz "the parity is odd\n"

	.text
main:
	li		$v0, 5
	syscall
	move	$t0, $v0
	# input is in $t0

	li 		$t1, 0 						# int bit_idx = 0;
	li 		$t2, 0						# int n_bits_set = 0;

loop1: 
	beq 	$t1, 32, body_end
	srav 	$t4, $t0, $t1 				# int bit = (n >> bit_idx) & 1; $t5 = bit
	andi 	$t5, $t4, 1
	add		$t2, $t2, $t5 				# n_bits_set = n_bits_set + bit;
	addi 	$t1, $t1, 1					# bit_idx++;
	b		loop1

body_end:
	rem 	$t4, $t2, 2					# if (n_bits_set % 2 != 0) {
	beq 	$t4, 0, else
	
	la		$a0, odd_parity_str			# printf ("the parity is odd\n");
	li 		$v0, 4
	syscall

	b 		end
else:
	li 		$v0, 4
	la 		$a0, even_parity_str		# printf ("the parity is even\n");
	syscall

end:

	li		$v0, 0
	jr		$ra
