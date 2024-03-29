#include<stdio.h>
#include <math.h>
#define SIZE 50

char stackc[SIZE];
int stacki[SIZE];
int topi = -1;
int top=-1;

int isEmpty(){ //check if stack is empty
	
    if(top < 0){
		return 1;
	}
	else
	{
		return 0;
	}
}

void push(char temp){ //push to my character stack
	
	if (top>= SIZE-1){
		
		printf("Stack Overflow\n");
	}
	else{	
       stackc[++top]=temp;
	}
}
char pop(){ //pop from my character stack
	 
	if (top<0){
		
		printf("Stack Underflow\n"); 
	}	
  else{
  		return stackc[top--];
  	}
}

void push_int(int n){ //push to my integer stack
	
	if (topi>= SIZE-1){
		
		printf("Stack Overflow\n");
	}
	else{	
       stacki[++topi]=n;
	}
}
int pop_int(){ //pop from my integer stack
	
	if (topi<0){
		
		printf("Stack Underflow\n"); 
	}	
  else{
  		return stacki[topi--];
  	}
}

int symbol_check (char item ){  //determine whether we have a symbol
	
	if(item == '^'|| item == '*'|| item =='+'|| item == '/'|| item == '-'|| item == '%'){
		
		return 1;
	}
	else if (item == '('||item == ')'){
		return 2;
	}
	else{
		return 0;
	}
}

int whos_stronger(char item){  //determine the priority
	
    if(item =='%'|| item =='*' || item =='/'){
       return 3;   //highest priority
   }
   
	else if(item =='+' || item =='-'){
       return 2; //second highest priority
   }
	
   else if(item =='('){
   	
       return 1; //last priority
   }
return 0;
}
int eval(int num1, int num2, char item){ //evaluate to find the value
	
	if(item =='+'){
		return(num1+num2);
	}
	else if(item == '-'){
		return(num2-num1);
	}
	else if(item == '*'){
		return(num2*num1);
	}
	else if(item == '/'){
		return(num1/num2);
	}
	else if(item =='%'){
		return(num2%num1);
	}
	else if(item == '^'){
		return(pow (num2, num1));
	}
	return 0;
}
int postfix_Evaluation(char postfix[]){ //evaluate the postfix
	char item;
    int i=0, num1, num2, val, finalval;
	
	for(i=0; postfix[i] != '\0'; i++){
		item = postfix[i];

		if(symbol_check(item) == 0){
			push_int(item-'0');
		}
		else if(symbol_check(item)==1){
			num1 = pop_int();
			num2 = pop_int();
			val = eval(num1, num2, item);
			push_int(val);
		}			
	}
	finalval = pop_int();
	printf("%d", finalval);
return 0;
}



int main(){
	
   char infix[SIZE],postfix[SIZE];
   char item;
   int i=0,j=0;
   
   printf("Please Enter an Infix Expression: ");
   gets(infix);
   
   
   while((item=infix[i])!='\0'){
       i++;
       if(symbol_check(item) == 0){ //if we don't return a symbol, then send to postfix
           postfix[j]=item;
           j++;
       }

       else if(symbol_check(item) == 1)  //check priority of symbols and if the stack if empty. Then push/pop accordingly
       {
           if(isEmpty() == 1){
               push(item);
           } 
           else if(whos_stronger(item)>whos_stronger(stackc[top])){
           		push(item);
		   }
           else{
               while(whos_stronger(item)<whos_stronger(stackc[top])){
                   postfix[j]=pop();
                   j++;
               }
               push(item);
           }
       }
       
        else if(item=='('){  
           push(item);
       }
       else if(item==')'){
           while(stackc[top]!='('){   
               postfix[j]=pop();
               j++;
           }
           top--;
       }
   }
   while(top!=-1){         
       postfix[j]=pop();
       j++;
   }
  
   printf("The Postfix Expression is: ");
   puts(postfix);
   
   printf("The Postfix Evaluated is Equivelent to: ");
   postfix_Evaluation(postfix);
  
}
