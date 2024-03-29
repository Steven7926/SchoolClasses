#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>

int cacheSize;
int association;
int repPolicy;
int writeBack;
int num_sets;
int hit = 0;
int miss = 0;
char *files_string;
long long int memory[100000][10000];

void parse_stuff(char * input, char * sim)
{
    int i = 0;
    char *cache;
    char *assoc;
    char *rep;
    char *write;

   // Extract the strings
   char * token = strtok(input, " ");

   while( token != NULL ) 
   {
      if (i == 1)
        cache = token;
  
      if (i == 2)
        assoc = token;

      if (i == 3)
          rep = token;

      if (i == 4)
          write = token;

      if (i == 5)
          files_string = token;

      token = strtok(NULL, " ");
      i++;
   }
    // Convert strings to ints
    cacheSize = atoi(cache);
    association = atoi(assoc);
    repPolicy = atoi(rep);
    writeBack = atoi(write);
}

long long int convertHextoInt(char * address)
{
    long long int addy = 0;
    long long int binary;
    int i;
    int val;
    int len;

    len = strlen(address) - 3;

    for (i = 2; address[i] != '\0'; i++)
    {
        if (address[i] >= '0' && address[i] <= '9')
            val = address[i] - 48;
        else if (address[i] >= 'a' && address[i] <= 'f')
            val = address[i] - 87;
        
        addy += val * pow(16, len);
        len--;
    }
    
    printf("\nHexnum: %s", address);
    printf("\ndecnum: %lld", addy);

    return addy;
}


void simulate_access(char op, long long int add, long long int tag_arr[num_sets][association], long long int cache[num_sets][association], bool dirty[num_sets][association])
{
    int set = (add/64)%num_sets;
    int i;
    long long int tag = add/64;

    for (i = 0; i < association; i++)
    {
        if (tag == tag_arr[set][i])
        {
            hit++;
        }
        else
        {
            miss++;
            if (repPolicy == 0)
                updateLRU(add);
            else if (repPolicy == 1)
                updateFIFO(add);
        }
    }
}

void updateLRU(lru_arr[num_sets][association])
{

    
}
void updateFIFO()
{
    
}



void main()
{

    char op;
    char input[150] = "./SIM 32768 8 1 1 C:/Users/steve/Desktop/Code/Project1CompArch/XSBENCH.t";
    char *sim;
    char endch;
    char address[20];
    long long int add;
    FILE* file;

    parse_stuff(input, sim);
    num_sets = (cacheSize/64)/8;

    //Arrays [row][column]
    long long int tag_arr[num_sets][association];
    long long int cache_arr[num_sets][association];
    bool dirty[num_sets][association];

    file = fopen(files_string, "r");

    op = getc(file);
    fscanf(file, "%s", &address);
    add = convertHextoInt(address);

    // while (endch != EOF)
    // {
 
    // }
    fclose(file);
}
