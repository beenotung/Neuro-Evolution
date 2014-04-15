#include <stdio.h>
#include <stdlib.h>
#include <vector>
#include <math.h>
using namespace std;
#include "utility.h"

#define Ntest   10                  //number of tesing data
#define Ninp    2                   //number of input node
#define Nout    1                   //number of output node
#define Nhid    (Ninp+Nout+1)/2+1   //number of node in each hidden layer
#define Nhl     1                   //number of hidden layer
#define Nlayer  1+Nhl+1             //number of layer
#define eta     0.5                 //intense to learn from new data
#define alpha   0.5                 //inertia (similar to swarm algorithm)
#define weight_file "test.wei"
#define test_file   "test.tra"
#define recall_file "test.rec"

//float random_value(void);

struct testnodetype
{
    vector<float> W;
    float Q,Output;
};

void BackNN_test()
{
    FILE    *fp1,*fp2,*fp3;
    vector<testnodetype> node[Nlayer];      //node[layer in the NN][node in a layer]
    float   T[Nout];                    //Training target output (sample)
    float   sum,mse;
    int     Itest;
    int     Ilayer,Inx,Iny;             //Inodex and Inodey

    /*----- open files -------*/
    fp1=fopen(weight_file,"r");
    fp2=fopen(test_file,"r");
    fp3=fopen(recall_file,"w");
    if (fp1==NULL)
    {
        puts("File not exist !!");
        exit(1);
    }
    if (fp2==NULL)
    {
        puts("File not exist !!");
        exit(1);
    }

    /*----- input vector node[] form weight_file ----*/
    for (Ilayer=1; Ilayer<Nlayer; Ilayer++)
    {
        float W,Q;
        for (Iny=0; Iny<Lsize(Ilayer,Ninp,Nout); Iny++)
        {
            node[Ilayer].clear();
            testnodetype dnode;
            dnode.W.clear();
            for (Inx=0; Inx<Lsize(Ilayer-1,Ninp,Nout); Inx++)
            {
                fscanf(fp1,"%f",&W);
                dnode.W.push_back(W);
            }
            fscanf(fp1,"%f",&Q);
            dnode.Q=Q;
            node[Ilayer].push_back(dnode);
        }
    }

    /*---------- Start Testing ---------*/    
    fseek(fp2,0,0);
    for (Itest=0; Itest<Ntest; Itest++)
    {        
        /*..... input one testing example ....*/
        for (Inx=0; Inx<Ninp; Inx++)
            fscanf(fp2,"%f",&node[0].[Inx].Output);

        for (Iny=0; Iny<Nout; Iny++)
            fscanf(fp2,"%f",&T[Iny]);

        /*..... compute Output .....*/
        for (Ilayer=1; Ilayer<Nlayer; Ilayer++)
        {
            for (Iny=0; (unsigned)Iny<node[Ilayer-1].size(); Iny++)
            {
                sum=0.0;
                for (Inx=0; (unsigned)Inx<node[Ilayer-1].size(); Inx++)
                    sum+=node[Ilayer-1][Inx].Output*node[Ilayer][Iny].W[Inx];
                node[Ilayer][Iny].Output=(float)1.0/(1.0+exp(-(sum-node[Ilayer][Iny].Q)));
            }
        }

        /*..... compute the mean_square_error ....*/
        mse=0.0;
        for (Iny=0; Iny<Nout; Iny++)
            mse+=(T[j]-node[Nlayer-1][Iny].Output)*(T[j]-node[Nlayer-1][Iny].Output);

        ///
        /*---- Write the results to recall_file -----*/
        printf("T[%d]= ",Itest);
        fprintf(fp3,"T[%d]= ",Itest);
        for (Iny=0; Iny<Nout; Iny++)
        {
            printf("%-8.2f\t",T[j]);
            fprintf(fp3,"%-8.2f\t",T[j]);
        }
        printf("Y[%d]= ",Itest);
        fprintf(fp3,"Y[%d]= ",Itest);
        for (Iny=0; Iny<Nout; Iny++)
        {
            printf("%-8.2f\t",node[Nlayer-1][Iny].Output);
            fprintf(fp3,"%-8.2f\t",node[Nlayer-1][Iny].Output);
        }
        printf("  mse= %-8.4f\n\n",mse);
        fprintf(fp3,"  mse= %-8.4f\n\n",mse);
        fprintf(fp3,"\n");
    }   /* end of recalling for total test example */

    fclose(fp1);
    fclose(fp2);
    fclose(fp3);

}   /* end of the program */
