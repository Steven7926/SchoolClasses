/* COP 3502C Final term Assignment 1
This program is written by: Steven Hudson*/

#include <stdio.h>
#include <stdlib.h>

void write_file(){
	
	printf("\n\nWriting Sorted Array to File...");
}

int merge_num(int x, int y){
	
int temp = y;
	do
	{
    	temp /= 10;
    	x *= 10;
	} while (temp >0);
	
	if(x<0 && y>0){
		y*=-1;
	}
	if(x>0 && y<0){
		x*=-1;
	}
	
return x + y;
}

int split(int place, int number){
    while(place--){	
        number/=10;
    }
    return number%10;
}


int binarySearch(int arr[], int l, int r, int x) 
{ 
    if (r >= l) { 
        int mid = l + (r - l) / 2; 
        if (arr[mid] == x) 
            return mid; 

        if (arr[mid] > x) 
            return binarySearch(arr, l, mid - 1, x); 
        return binarySearch(arr, mid + 1, r, x); 
    } 
  
    return -1; 
} 



void merge(int arr[], int l, int m, int r)
{
    int i, j, k;
    int n1 = m - l + 1;
    int n2 =  r - m;

    /* create temp arrays */
    int L[n1], R[n2]; //if your compiler does not support this, create them dynamically.

    /* Copy data to temp arrays L[] and R[] */
    for (i = 0; i < n1; i++)
        L[i] = arr[l + i];
    for (j = 0; j < n2; j++)
        R[j] = arr[m + 1+ j];

    /* Merge the temp arrays back into arr[l..r]*/
    i = 0; // Initial index of first subarray
    j = 0; // Initial index of second subarray
    k = l; // Initial index of merged subarray
    while (i < n1 && j < n2)
    {
        if (L[i] <= R[j])
        {
            arr[k] = L[i];
            i++;
        }
        else
        {
            arr[k] = R[j];
            j++;
        }
        k++;
    }

    /* Copy the remaining elements of L[], if there
       are any */
    while (i < n1)
    {
        arr[k] = L[i];
        i++;
        k++;
    }

    /* Copy the remaining elements of R[], if there
       are any */
    while (j < n2)
    {
        arr[k] = R[j];
        j++;
        k++;
    }
}


void mergeSort(int arr[], int l, int r)
{
    if (l < r)
    {
        // Same as (l+r)/2, but avoids overflow for
        // large l and h
        int m = l+(r-l)/2;

        // Sort first and second halves
        mergeSort(arr, l, m);
        mergeSort(arr, m+1, r);

        merge(arr, l, m, r);
    }
}

void printArray(int A[], int size)
{
    int i;
    for (i=0; i < size; i++)
        printf("%d ", A[i]);
    printf("\n");
}


void read_sort_write(){
	
	FILE *file_inp;	
	FILE *file_out;
	file_inp = fopen("C:\\test.txt", "r");
	file_out = fopen("C:\\testwrite.txt", "w");
    int num_pairs, num_ints, i,j=0, x=1;
	
	if(file_inp ==NULL){
		printf("Error Reading File");
	}
	
		
	fscanf(file_inp, "%d", &num_pairs);
	printf("\tNumber of Pairs: %d\n", num_pairs);
	num_ints = 2*num_pairs;
	int *arr = (int *)malloc(num_ints*sizeof(int));
	int *arrymerged = (int *)malloc(num_ints*sizeof(int));
	int *usrinp = (int *)malloc(num_ints*sizeof(int));
	
	printf("\tPairs: ");
	for (i=0; i<num_ints; i++){
		fscanf(file_inp, "%d", &arr[i]);
		if(i == 0){
			printf(" %d ", arr[0]);
		}
		else if(i%2 == 0){
			printf("\n\t\t%d ", arr[i]);
		}
		else{
			printf("%d", arr[i]);
		}
	}
	fclose(file_inp);
	for(i=0; i<num_ints; i++){    //merge the x and y and treat them as one number to be sorted. 
		int counter = 0;
			if(i%2==0){	
				while(counter!=1){
					arrymerged[j] = merge_num(arr[i], arr[i+1]);	
					counter++;
				}
				j++;
			}			
	}			
	

	
	printf("\n\nFile Successfully Read, Now Sorting...");	
	mergeSort(arrymerged, 0, num_pairs-1 );
	
	printf("\n\tPairs After Sorting: ");
	j=0;
	for(i = 0; i<num_pairs; i++){		
    	int x = split(1, arrymerged[i]);
    	int y = split( 0, arrymerged[i]);
	
		
		 	arr[j] = x;
    		arr[j+1] = y;
		 
    	if(i== 0){
			printf(" %d %d \n", arr[j], arr[j+1]);
			fprintf(file_out, "%d  %d\n", arr[j], arr[j+1]);
		}
		else{
			printf("\t\t\t      %d %d \n", arr[j], arr[j+1]);
			fprintf(file_out, "%d  %d\n", arr[j], arr[j+1]);
		}		
	}	
		fclose(file_out);
	printf("Output Written to File.\n\n");

	while(x=1){
	
		printf("Search input (x y , -1 -1 to exit): ");
		scanf("%d %d", &usrinp[0], &usrinp[1]);
	
		int usrinp_merge = merge_num(usrinp[0], usrinp[1]);
		int foundornot = binarySearch(arrymerged, 0, num_pairs-1, usrinp_merge) + 1;	
	
		if(foundornot== 0){
			printf("\n\t\tNot Found\n\n");
		}
		else{
			printf("\n\t\tFound at Record %d\n\n", foundornot);
		}
		if(usrinp[0] == -1 && usrinp[1] == -1){
			break;
		}
	}

}

int main(){
	printf("Reading Input File...\n");
	read_sort_write();
	return 0;
	
}
