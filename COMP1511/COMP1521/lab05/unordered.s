# Read 10 numbers into an array
# print 0 if they are in non-decreasing order
# print 1 otherwise

# i in register $t0

main:

    li   $t0, 0         # i = 0
loop0:
    bge  $t0, 10, end0  # while (i < 10) {

    li   $v0, 5         #   scanf("%d", &numbers[i]);
    syscall             #

    mul  $t1, $t0, 4    #   calculate &numbers[i]
    la   $t2, numbers   #
    add  $t3, $t1, $t2  #
    sw   $v0, ($t3)     #   store entered number in array

    addi $t0, $t0, 1    #   i++;
    j    loop0          # }
end0:
    li   $t1, 0         # int swapped = 0;

    li   $t0, 1         # i = 1;

loop1: 
    bge  $t0, 10, end1  # while (i < 10) {
    
    mul  $t2, $t0, 4    # calculate numbers[i]
    la   $t3, numbers   # $t3 = &numbers
    add  $t4, $t2, $t3  # $t4 = &numbers[i]
    lw   $t5, ($t4)     # $t5 = numbers[i]

    sub  $t2, $t4, 4    # $t2 = &numbers[i - 1]
    lw   $t6, ($t2)     # $t6 = numbers[i - 1]

    bge  $t5, $t6, skip # if (numbers[i] >= numbers[i - 1]) goto skip

    li   $t1, 1         # swapped = 1;

skip: 
    addi $t0, $t0, 1    # i++;

    j    loop1
end1:
    move   $a0, $t1     # printf("%d", swapped)
    li   $v0, 1         #
    syscall

    li   $a0, '\n'      # printf("%c", '\n');
    li   $v0, 11
    syscall

    jr   $ra

.data

numbers:
    .word 0 0 0 0 0 0 0 0 0 0  # int numbers[10] = {0};

