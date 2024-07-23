# read 10 numbers into an array
# bubblesort them
# then print the 10 numbers

# i in register $t0
# registers $t1, $t2 & $t3 used to hold temporary results

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
    b    loop0          # }
end0:
    li   $t5, 1         # swapped = 1
swapped_loop:
    beq  $t5, 0, swapped_end
    li   $t5, 0         # swapped = 0
    li   $t0, 1         # i = 1
swapped_loop2:
    bge  $t0, 10, swapped_end2

    mul  $t1, $t0, 4    #   calculate &numbers[i]
    la   $t2, numbers   #
    add  $t3, $t1, $t2
    lw   $t6, ($t3)     # x = numbers[i]

    sub  $t4, $t3, 4    # $t4 = &numbers[i - 1]
    lw   $t7, ($t4)     # y = numbers[i - 1]

    bge  $t6, $t7, skip
    sw   $t7, ($t3)
    sw   $t6, ($t4)
    li   $t5, 1
skip:
    addi $t0, $t0, 1
    b    swapped_loop2

swapped_end2: 
    b    swapped_loop

swapped_end:
    li   $t0, 0         # i = 0
loop1:
    bge  $t0, 10, end1  # while (i < 10) {

    mul  $t1, $t0, 4    #   calculate &numbers[i]
    la   $t2, numbers   #
    add  $t3, $t1, $t2  #
    lw   $a0, ($t3)     #   load numbers[i] into $a0
    li   $v0, 1         #   printf("%d", numbers[i])
    syscall

    li   $a0, '\n'      #   printf("%c", '\n');
    li   $v0, 11
    syscall

    addi $t0, $t0, 1    #   i++
    j    loop1          # }
end1:

    jr   $ra            # return

.data

numbers:
    .word 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  # int numbers[10] = {0};

