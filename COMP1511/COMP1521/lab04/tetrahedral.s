# Read a number n and print the first n tetrahedral numbers
# https://en.wikipedia.org/wiki/Tetrahedral_number

main:                  # int main(void) {

    la   $a0, prompt   # printf("Enter how many: ");
    li   $v0, 4
    syscall

    li   $v0, 5         # scanf("%d", &howmany);
    syscall

    move $t0, $v0       # $t0 = howmany;

    li   $t1, 1         # n = 1;

loop1:
    bgt  $t1, $t0, end  # if(n > howmany) goto end;
    
    li   $t2, 0         # total = 0;

    li   $t3, 1         # j = 1;

    b    loop2   

main1: 
    move $a0, $t2        #   printf("%d", total);
    li   $v0, 1
    syscall

    li   $a0, '\n'      # printf("%c", '\n');
    li   $v0, 11
    syscall

    addi $t1, $t1, 1    # n = n + 1;

    b    loop1

loop2:
    bgt  $t3, $t1, main1 # if(j > n) goto main1;

    li   $t4, 1          # i = 1;

    b    loop3
main2:
    addi $t3, $t3, 1     # j = j + 1;

    b    loop2

loop3:
    bgt  $t4, $t3, main2  # if(i > j) goto main2;
    
    add  $t2, $t2, $t4    # total = total + i;

    addi $t4, $t4, 1      # i = i + 1;

    b    loop3

end:
    li   $v0, 0
    jr   $ra           # return 0

    .data
prompt:
    .asciiz "Enter how many: "
