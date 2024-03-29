#include <stdlib.h>
#include <stdio.h>


typedef struct node{
	int info;
	struct node* next, *prev;
}node;


node* generate_list(node* root, int n){
	int i;
	node* last;
	last= (node*)malloc(sizeof(node)); //last node
		
	for(i = 1; i <n; i++){
		node* temp;
		temp = (node*)malloc(sizeof(node));
		temp->info = i;
		temp->next = NULL;
		temp->prev = NULL;
		if(i==1){			//set root 
			root->info = i;
			root->prev = last;  //point root to last
			last = root;
		}
		else{
			last->next = temp;
			last->prev = temp;
			last=temp;
		}
	}
	node* temp = (node*)malloc(sizeof(node)); //create the last node and point it to the first node.
	temp->info= n;
	last->next = temp;
	last->prev = temp;
	last = temp;
	last->next = root;	
	
		
			
	return root;
}

int josephus_solved(int n, int k){	
	int i;
		node* temp;
		node* t;
		node* del; //node to delete
		
		temp = (node*)malloc(sizeof(node));
		t = (node*)malloc(sizeof(node));
			
		temp = generate_list(t, n); 
		
		printf("\nPeople are in positions: ");
		while(temp->info!=n){
        	printf("%d  ", temp->info);
        	temp= temp->next;
    	}
    printf(" %d", n);
	printf("\n");
		
		printf("\tOrder of killing: \n");
		
		while(t->next!=t && t!=NULL){
			for(i=1; i<=k-1; i++){
				temp = t;
				t = t->next;
			}  //find the node that needs to be deleted and delete it
			temp->next = temp->next->next;
			del= t;
			t = t->next;
			printf("\tPerson in position %d killed\n", del->info);
			free(del);
		}				
		printf("Last alive is person in position %d", t->info);

}

int main(void){
	int n,k;
	
	printf("Enter n (number of people in the circle): ");
	scanf("%d", &n);
	printf("Enter k (number of people to be skipped): ");
	scanf("%d", &k);
	
	if(n<=0||k<=0){
		printf("\n\tPlease enter a valid number (non-zero, non-negative).");
	}
	else if(n==1){
		printf("Last alive is person in position %d", n);
	}
	else{	
		josephus_solved(n, k);
	}
	
	return 0;
}

