.MODEL SMALL
.STACK 1000H
.Data
	number DB "00000$"
	i DW 1 DUP (0000H)
	j DW 1 DUP (0000H)
.CODE
main PROC
	MOV AX, @DATA
	MOV DS, AX
	PUSH BP
	MOV BP, SP
	SUB SP, 2
	SUB SP, 2
	SUB SP, 2
	SUB SP, 2
	SUB SP, 2
	SUB SP, 2
L1:
	MOV AX, 2       ; Line 5
	MOV i, AX
L2:
	MOV AX, 3       ; Line 6
	MOV j, AX
L3:
	MOV AX, i       ; Line 7
	MOV CX, AX
	MOV AX, 2       ; Line 7
	CWD
	MUL CX
	PUSH AX
	MOV AX, j       ; Line 7
	MOV CX, AX
	MOV AX, 3       ; Line 7
	CWD
	MUL CX
	PUSH AX
	POP AX       ; Line 7
	MOV DX, AX
	POP AX       ; Line 7
	ADD AX, DX
	PUSH AX
	POP AX       ; Line 7
	MOV [BP-2], AX
L4:
	MOV AX, [BP-2]       ; Line 8
	CALL print_output
	CALL new_line
L5:
	MOV AX, 9       ; Line 17
	MOV CX, AX
	MOV AX, [BP-2]       ; Line 17
	CWD
	DIV CX
	PUSH DX
	POP AX       ; Line 17
	MOV [BP-6], AX
L6:
	MOV AX, [BP-6]       ; Line 18
	CALL print_output
	CALL new_line
L7:
L8:
	ADD SP, 12
	POP BP
	MOV AX,4CH
	INT 21H
main ENDP
;-------------------------------
;         print library         
;-------------------------------
new_line proc
    push ax
    push dx
    mov ah,2
    mov dl,0Dh
    int 21h
    mov ah,2
    mov dl,0Ah
    int 21h
    pop dx
    pop ax
    ret
    new_line endp
print_output proc  ;print what is in ax
    push ax
    push bx
    push cx
    push dx
    push si
    lea si,number
    mov bx,10
    add si,4
    cmp ax,0
    jnge negate
    print:
    xor dx,dx
    div bx
    mov [si],dl
    add [si],'0'
    dec si
    cmp ax,0
    jne print
    inc si
    lea dx,si
    mov ah,9
    int 21h
    pop si
    pop dx
    pop cx
    pop bx
    pop ax
    ret
    negate:
    push ax
    mov ah,2
    mov dl,'-'
    int 21h
    pop ax
    neg ax
    jmp print
    print_output endp
;-------------------------------
END main
