/* COP 3502C Final term Assignment 2
This program is written by: Steven Hudson */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define max_length 20

FILE *inpfile;
FILE *outfile;

struct tree_node  
{ 
    char *data; 
    struct tree_node *left; 
    struct tree_node *right; 
}; 

struct tree_node* newNode(char *data) {
  struct tree_node *temp;
  temp  = (struct tree_node*)malloc(sizeof(struct tree_node)); 
  
  temp->data = data; 
  temp->left = NULL; 
  temp->right = NULL; 
  return(temp); 
} 

struct tree_node* insert(struct tree_node *root, struct tree_node *element) {

	if (root == NULL){
		return element;
	}
	else {
		if (strcmp(element->data, root->data)>0){
			
			if (root->right != NULL){
				root->right = insert(root->right, element);
			}
			else{
				root->right = element;
			}
		}

		else {
			if (root->left != NULL){
				root->left = insert(root->left, element);
			}
			else{			
				root->left = element;
			}
		}
		return root;
	}
}


struct tree_node * smallest_leaf(struct tree_node* root){ 

    struct tree_node* current = root;  //loop down to find smallest leaf
    while (current->left != NULL){
        current = current->left; 
	}
    return current; 
} 

struct tree_node* deletenode(struct tree_node* root, char *element) 
{ 
    if (root == NULL){
    	return root;
	}	    
    if (strcmp(element, root->data)<0){ //left subtree
        root->left = deletenode(root->left, element); 
	}	
    else if (strcmp(element, root->data)>0){ //right subtree
        root->right = deletenode(root->right, element); 
	}
	
    else{ // equal, brgin deleting  
	    
        if (root->left == NULL){  //handles one child or no child 
            struct tree_node *temp = root->right; 
            free(root); 
            printf("Deleted");
            fprintf(outfile, "Deleted");
            return temp; 
        } 
        else if (root->right == NULL){
            struct tree_node *temp = root->left; 
            free(root); 
            printf("Deleted");
            fprintf(outfile, "Deleted");
            return temp; 
        } 
        
        struct tree_node* temp = smallest_leaf(root->right); //handles two children 
        root->data = temp->data; 
        root->right = deletenode(root->right, temp->data); 
    } 
    return root; 
} 

int compare(struct tree_node *current, char *element){	//compares the strings until they equal each other, then returns
	if(current!=NULL){
		if(strcmp(element, current->data)==0){	 //if equal
			return 1;
		}
		if(strcmp(element, current->data)<0){ //if less
			return compare(current->left, element);
		}
		else{ //if greater
			return compare(current->right, element);
		}	
	}
	else{
		return 0;
	}
}
int counter =1;
void count_before(struct tree_node *root, char *element){ 	//counts the names before the found name
	if(root!=NULL){
	
		if(strcmp(element, root->data)==0){
			printf("%d", counter);
			fprintf(outfile, "%d", counter);
			
		}
		if(strcmp(element, root->data)<0){
			++counter;
		    count_before(root->left, element);			
		}
		else{
			++counter;
			count_before(root->right, element);
		}
	}
			
}

void preorder (struct tree_node *x){ //preorder traversal function
	if (x != NULL) {
		fprintf(outfile, "%s ", x->data);
		printf("%s " , x->data);
		preorder(x->left);
		preorder(x->right);
	}
}
void inorder(struct tree_node*x) { //inorder traversal function
	if (x != NULL)
	{
		inorder(x->left);
		fprintf(outfile, "%s ", x->data);
		printf("%s ", x->data);
		inorder(x->right);
	}
}
void postorder (struct tree_node*x){ //postorder traversal function
		if(x!=NULL){
			postorder(x->left);
			postorder(x->right);
			fprintf(outfile, "%s ", x->data);
			printf("%s ", x->data);
		}
}


int main(){
	
	printf("Reading Input File...");
	int num_names, num_search, num_del, i; 
	struct tree_node *my_root =NULL, *tempnode;
	inpfile = fopen("C:\\test.txt", "r");
	outfile = fopen("C:\\testwrite.txt", "w");
	for(i = 0; i<=2; i++){ //get the first 3 values of input values and set them respectively
		if (i==0){
			fscanf(inpfile, "%d", &num_names);
			printf("\n Number of Names: %d", num_names);
		}
		if(i==1){
			fscanf(inpfile, "%d", &num_search);
			printf("\n Number of Names to Search: %d", num_search);
		}
		if(i==2){
			fscanf(inpfile, "%d", &num_del);
			printf("\n Number of Names to Delete: %d\n", num_del);
		}
	}

	char temp[num_names];
	char names[num_names][max_length];
	printf(" List of Names: ");
	for(i=0; i<num_names; i++)	{
		fscanf(inpfile, "%s", temp);
		strcpy(names[i], temp);
		printf("%s ", names[i]);
	} //Now have an array, names[], to start inserting them into a tree
	
	for(i = 0; i<num_names; i++){
		tempnode = newNode(names[i]);
		my_root = insert(my_root, tempnode);
	}
	
	
	printf("\n\n\n\tPreorder: ");
	fprintf(outfile, "\nPreorder: ");
	preorder(my_root);
	printf("\n\tInorder: ");
	fprintf(outfile, "\nInorder: ");
	inorder(my_root);
	printf("\n\tPostorder: ");
	fprintf(outfile, "\nPostorder: ");
	postorder(my_root);	
	printf("\n\n\tSearch Phase ");
	fprintf(outfile, "\nSearch Phase: ");
	char temp1[num_search];
	char name_search[num_search][max_length];
	
	
	for(i=0; i<num_search; i++)	{ //get the search names and put them in an array of strings
		fscanf(inpfile, "%s", temp1);
		strcpy(name_search[i], temp1);
		printf("\n\t\t%s: ", name_search[i]);
		fprintf(outfile, "\n%s: ", name_search[i]);		
		int x =compare(my_root, name_search[i]);
			if(x == 1){
				printf("Found, items before: ");
				fprintf(outfile, "Found, items before: ");
				count_before(my_root, name_search[i]);
			}
			else{
				printf("Not found, items before: 0");
				fprintf(outfile, "Not found, items before: 0");
			}
	}
	
	printf("\n\n\tDelete Phase ");
	fprintf(outfile, "\nDelete Phase: ");
	char temp2[num_del];
	char name_delete[num_del][max_length];
	
	for(i=0; i<num_del; i++){ 		//get the names to delete and store them in an array of strings
		fscanf(inpfile, "%s", temp2);
		strcpy(name_delete[i], temp2);
		printf("\n\t\t%s: ", name_delete[i]);
		fprintf(outfile, "\n%s: ", name_delete[i]);
		my_root = deletenode(my_root, name_delete[i]);
	}
	fclose(inpfile);
	printf("\n\n\tNew Order After Deletion");
	printf("\n\t\tPreorder: ");
	fprintf(outfile, "\nPreorder: ");
	preorder(my_root);
	printf("\n\t\tInoderder: ");
	fprintf(outfile, "\nInorder: ");
	inorder(my_root);
	printf("\n\t\tPostorder: ");
	fprintf(outfile, "\nPostorder: ");
	postorder(my_root);	
	fclose(outfile);
	return 0;	
}
