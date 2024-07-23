# read a mark and print the corresponding UNSW grade

main:
    la   $a0, prompt    # printf("Enter a mark: ");
    li   $v0, 4
    syscall

    li   $v0, 5         # scanf("%d", mark);
    syscall

    bge  $v0, 50, else1 # if(mark >= 50) goto else1;

    la   $a0, fl        # printf("FL\n");
    li   $v0, 4
    syscall
    
    b    end

else1:
    bge  $v0, 65, else2 # if(mark >= 65) goto else2;

    la $a0, ps          # printf("PS\n");
    li $v0, 4
    syscall

    b    end

else2:
    bge $v0, 75, else3  # if(mark >= 75) goto else3;

    la $a0, cr          # printf("CR\n");
    li $v0, 4
    syscall

    b    end

else3:
    bge $v0, 85, else   # if(mark >= 85) goto else;

    la $a0, dn          # printf("DN\n");
    li $v0, 4
    syscall

    b    end

else: 
    la $a0, hd          # printf("HD\n");
    li $v0, 4
    syscall

end:
    jr   $ra            # return

    .data
prompt:
    .asciiz "Enter a mark: "
fl:
    .asciiz "FL\n"
ps:
    .asciiz "PS\n"
cr:
    .asciiz "CR\n"
dn:
    .asciiz "DN\n"
hd:
    .asciiz "HD\n"
