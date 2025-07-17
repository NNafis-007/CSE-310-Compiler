.MODEL SMALL
.STACK 1000H
.DATA
	number DB "00000$"
.CODE
main PROC
	MOV AX, @DATA
	MOV DS, AX
	PUSH BP
	MOV BP, SP

	SUB SP , 2
	MOV AX, [BP-2]
	PUSH AX
	MOV AX, 0		; Line 3
	POP DX
	CMP DX, AX
	JG L1
	JMP L2
L1:
	MOV AX, 1
	PUSH AX
	JMP L3
L2:
	MOV AX, 0
	PUSH AX
L3:
	MOV AX, [BP-2]
	PUSH AX
	MOV AX, 10		; Line 3
	POP DX
	CMP DX, AX
	JL L4
	JMP L5
L4:
	MOV AX, 1
	PUSH AX
	JMP L6
L5:
	MOV AX, 0
	PUSH AX
L6:
	POP AX
	POP DX
	CMP DX, 0
	JNE L7
	CMP AX, 0
	JNE L7
	MOV AX, 0
	PUSH AX
	JMP L8
L7:
	MOV AX, 1
	PUSH AX
	JMP L8
L8:
	POP AX
	CMP AX, 0
	JE L9
	MOV AX, 100		; Line 4
	MOV [BP-2], AX		; Line 4

	JMP L10
L9:
	MOV AX, 200		; Line 6
	MOV [BP-2], AX		; Line 6

L10:
	MOV AX, [BP-2]
	PUSH AX
	MOV AX, 20		; Line 8
	POP DX
	CMP DX, AX
	JG L11
	JMP L12
L11:
	MOV AX, 1
	PUSH AX
	JMP L13
L12:
	MOV AX, 0
	PUSH AX
L13:
	MOV AX, [BP-2]
	PUSH AX
	MOV AX, 30		; Line 8
	POP DX
	CMP DX, AX
	JL L14
	JMP L15
L14:
	MOV AX, 1
	PUSH AX
	JMP L16
L15:
	MOV AX, 0
	PUSH AX
L16:
	POP AX
	POP DX
	CMP DX, 0
	JE L17
	CMP AX, 0
	JE L17
	MOV AX, 1
	PUSH AX
	JMP L18
L17:
	MOV AX, 0
	PUSH AX
	JMP L18
L18:
	POP AX
	CMP AX, 0
	JE L19
	MOV AX, 300		; Line 9
	MOV [BP-2], AX		; Line 9

	JMP L20
L19:
	MOV AX, 400		; Line 11
	MOV [BP-2], AX		; Line 11

L20:
	MOV AX, [BP-2]
	PUSH AX
	MOV AX, 40		; Line 13
	POP DX
	CMP DX, AX
	JG L21
	JMP L22
L21:
	MOV AX, 1
	PUSH AX
	JMP L23
L22:
	MOV AX, 0
	PUSH AX
L23:
	MOV AX, [BP-2]
	PUSH AX
	MOV AX, 50		; Line 13
	POP DX
	CMP DX, AX
	JL L24
	JMP L25
L24:
	MOV AX, 1
	PUSH AX
	JMP L26
L25:
	MOV AX, 0
	PUSH AX
L26:
	POP AX
	POP DX
	CMP DX, 0
	JE L27
	CMP AX, 0
	JE L27
	MOV AX, 1
	PUSH AX
	JMP L28
L27:
	MOV AX, 0
	PUSH AX
	JMP L28
L28:
	MOV AX, [BP-2]
	PUSH AX
	MOV AX, 60		; Line 13
	POP DX
	CMP DX, AX
	JL L29
	JMP L30
L29:
	MOV AX, 1
	PUSH AX
	JMP L31
L30:
	MOV AX, 0
	PUSH AX
L31:
	MOV AX, [BP-2]
	PUSH AX
	MOV AX, 70		; Line 13
	POP DX
	CMP DX, AX
	JG L32
	JMP L33
L32:
	MOV AX, 1
	PUSH AX
	JMP L34
L33:
	MOV AX, 0
	PUSH AX
L34:
	POP AX
	POP DX
	CMP DX, 0
	JE L35
	CMP AX, 0
	JE L35
	MOV AX, 1
	PUSH AX
	JMP L36
L35:
	MOV AX, 0
	PUSH AX
	JMP L36
L36:
	POP AX
	POP DX
	CMP DX, 0
	JNE L37
	CMP AX, 0
	JNE L37
	MOV AX, 0
	PUSH AX
	JMP L38
L37:
	MOV AX, 1
	PUSH AX
	JMP L38
L38:
	POP AX
	CMP AX, 0
	JE L39
	MOV AX, 500		; Line 14
	MOV [BP-2], AX		; Line 14

	JMP L40
L39:
	MOV AX, 600		; Line 16
	MOV [BP-2], AX		; Line 16

L40:
	MOV AX, [BP-2]		; Line 17
	CALL print_output
	CALL new_line

	MOV AX, 0		; Line 19
	JMP L41
L41:
	ADD SP, 2
	POP BP
	MOV AX, 4Ch
	INT 21h
main ENDP
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
END main
