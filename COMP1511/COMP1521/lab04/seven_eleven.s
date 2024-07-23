# Read a number and print positive multiples of 7 or 11 < n

main:                  # int main(void) {

    la   $a0, prompt   # printf("Enter a number: ");
    li   $v0, 4
    syscall

    li   $v0, 5         # scanf("%d", number);
    syscall

    move $t0, $v0       # t0 = number;

    li   $t1, 1         # int i = 1;
loop:
    bge  $t1, $t0, end  # if(i >= number) goto end;

    rem  $t2, $t1, 7    # if(i % 7 == 0) goto then1;
    beq  $t2, $zero, then1

    rem  $t3, $t1, 11   # if(i % 11 == 0) goto then1;
    beq  $t3, $zero, then1

    addi $t1, $t1, 1    # i++;

    b    loop

then1:
    move   $a0, $t1        #   printf("%d", i);
    li   $v0, 1
    syscall

    li   $a0, '\n'      # printf("%c", '\n');
    li   $v0, 11
    syscall

    addi $t1, $t1, 1    # i++;

    b    loop


end:
    li   $v0, 0
    jr   $ra           # return 0

    .data
prompt:
    .asciiz "Enter a number: "
