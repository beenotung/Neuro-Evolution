#include <stdio.h>
#include <stdlib.h>
#include <vector>
#include <math.h>
#include <time.h>
using namespace std;
#include "utility.h"

#define Ncycle  1000               //number of Learning Iteration
#define Ntrain  4                  //number of training example
#define Ninp    2                  //number of input node
#define Nout    1                  //number of output node
#define Nhid    (Ninp+Nout+1)/2+1  //number of node in each hidden layer
#define Nhl     1                  //number of hidden layer
#define Nlayer  1+Nhl+1            //number of layer
#define eta     0.5                //intense to learn from new data
#define alpha   0.5                //inertia (similar to swarm algorithm)
#define train_file  "test.tra"
#define weight_file "test.wei"
#define mse_file    "test.mse"

//float random_value(void);

struct trainnodetype
{
    vector<float> W,dW;
    float Q,dQ,delta,Output;
};

unsigned int Lsize_train(int i)
{
    if (i==0)
    {
        return Ninp;
    }
    else
    {
        if (i==Nlayer-1)
        {
            return Nout;
        }
        else
        {
            return Nhid;
        }
    }
}

void BackNN_train()
{
    FILE    *fp1,*fp2,*fp3;
    vector<trainnodetype> node[Nlayer];      //node[layer in the NN][node in a layer]
    float   T[Nout];                    //Training target output (sample)
    float   sum,mse;
    unsigned int     Icycle_train,Itrain;
    unsigned int     Ilayer,Inx,Iny;             //Inodex and Inodey
    long int now;

    /*----- open files -------*/
    fp1=fopen(train_file,"r");
    fp2=fopen(weight_file,"w");
    fp3=fopen(mse_file,"w");
    if (fp1==NULL)
    {
        puts("File not exist !!");
        exit(1);
    }

    /*----- initialize vector node[] e.g. weight, Q ----*/
    srand(time(&now)%1000);
    for (Ilayer=0; Ilayer<Nlayer; Ilayer++)
    {
        node[Ilayer].clear();
        for (Iny=0; Iny<Lsize_train(Ilayer); Iny++)
        {
            trainnodetype dnode;
            dnode.Q=0.0;          //??//random for the last one?
            dnode.dQ=0.0;
            dnode.delta=0.0;
            node[Ilayer].push_back(dnode);
        }
    }
    for (Ilayer=1; Ilayer<Nlayer; Ilayer++)
        for (Iny=0; Iny<node[Ilayer].size(); Iny++)
        {
            node[Ilayer][Iny].W.clear();
            node[Ilayer][Iny].dW.clear();
            for (Inx=0; Inx<node[Ilayer-1].size(); Inx++)
            {
                node[Ilayer][Iny].W.push_back(random_value());
                node[Ilayer][Iny].dW.push_back(0);
            }
        }

    /*-------- Start Learning ---------*/
    for (Icycle_train=0; Icycle_train<Ncycle; Icycle_train++)
    {
        mse=0.0;
        /*.. input one training example ...*/
        fseek(fp1,0,0);
        for (Itrain=0; Itrain<Ntrain; Itrain++)
        {
            for (Inx=0; Inx<Ninp; Inx++)
            {
                float input;
                fscanf(fp1,"%f",&input);
                node[0][Inx].Output=input;
            }
            for (Iny=0; Iny<Nout; Iny++)
            {
                fscanf(fp1,"%f",&T[Iny]);
            }

            /*..... compute Output .....*/
            for (Ilayer=1; Ilayer<Nlayer; Ilayer++)
                for (Iny=0; Iny<node[Ilayer].size(); Iny++)
                {
                    sum=0.0;
                    for (Inx=0; Inx<node[Ilayer-1].size(); Inx++)
                        sum+=node[Ilayer-1][Inx].Output*node[Ilayer][Iny].W[Inx];
                    node[Ilayer][Iny].Output=(float)1.0/(1.0+exp(-(sum-node[Ilayer][Iny].Q)));
                }

            /*..... compute delta .....*/
            for (Iny=0; Iny<node[Nlayer-1].size(); Iny++)
                node[Nlayer-1][Iny].delta=node[Nlayer-1][Iny].Output*(1-node[Nlayer-1][Iny].Output)*(T[Iny]-node[Nlayer-1][Iny].Output);

            for (Ilayer=Nlayer-2; Ilayer>0; Ilayer--)
                for (Inx=0; Inx<node[Ilayer].size(); Inx++)
                {
                    sum=0.0;
                    for (Iny=0; Iny<node[Ilayer+1].size(); Iny++)
                        sum+=node[Ilayer+1][Iny].W[Inx]*node[Ilayer+1][Iny].delta;
                    node[Ilayer][Inx].delta=node[Ilayer][Inx].Output*(1-node[Ilayer][Inx].Output)*sum;
                }

            /*..... compute dW,dQ .....*/
            for (Ilayer=Nlayer-1; Ilayer>0; Ilayer--)
                for (Iny=0; Iny<node[Ilayer].size(); Iny++)
                {
                    for (Inx=0; Inx<node[Ilayer-1].size(); Inx++)
                        node[Ilayer][Iny].dW[Inx]=eta*node[Ilayer][Iny].delta*node[Ilayer][Iny].Output+alpha*node[Ilayer][Iny].dW[Inx];
                    node[Ilayer][Iny].dQ=-eta*node[Ilayer][Iny].delta+alpha*node[Ilayer][Iny].dQ;
                }

            /*..... compute new W,Q .....*/
            for (Ilayer=Nlayer-1; Ilayer>0; Ilayer++)
                for (Iny=0; Iny<node[Ilayer].size(); Iny++)
                    for (Inx=0; Inx<node[Ilayer-1].size(); Inx++)
                        node[Ilayer][Iny].W[Inx]+=node[Ilayer][Iny].dW[Inx];

            for (Ilayer=Nlayer-1; Ilayer>0; Ilayer++)
                for (Iny=0; Iny<node[Ilayer].size(); Iny++)
                    node[Ilayer][Iny].Q+=node[Ilayer][Iny].dQ;

            /*... compute the mean_square_error ...*/
            for (Iny=0; Iny<node[Nlayer-1].size(); Iny++)
                mse+=(T[Iny]-node[Nlayer-1][Iny].Output)*(T[Iny]-node[Nlayer-1][Iny].Output);
        }   /* end of 1 learning cycle */

        /*... write the mse_value to mse_file ...*/
        mse/=Ntrain;
        if ((Icycle_train % 10)==9)
        {
            printf("\nIcycle_train=%3d mse=%-8.12f\n",Icycle_train,mse);
            fprintf(fp3,"%3d %-8.12f\n",Icycle_train,mse);
        }

    }   /* end of total learning cycle */

    /*---- Write the weights to weight_file -----*/
    printf("\n");

    for (Ilayer=1; Ilayer<Nlayer; Ilayer++)
    {
        for (Iny=0; Iny<Lsize_train(Ilayer); Iny++)
        {
            for (Inx=0; Inx<Lsize_train(Ilayer-1); Inx++)
            {
                printf("node[%d][%d].W[%d]=%-8.12f\t",Ilayer,Iny,Inx,node[Ilayer][Iny].W[Inx]);
                fprintf(fp2,"%-8.12f\t",node[Ilayer][Iny].W[Inx]);
            }
            printf("\n");
            fprintf(fp2,"\n");
        }
        printf("\n");
        fprintf(fp2,"\n");
    }

    printf("\nn");
    fprintf(fp2,"\nn");

    for (Ilayer=1; Ilayer<Nlayer; Ilayer++)
    {
        for (Iny=0; Iny<Lsize_train(Ilayer); Iny++)
        {
            for (Inx=0; Inx<Lsize_train(Ilayer-1); Inx++)
            {
                printf("node[%d][%d].Q=%-8.12f\t",Ilayer,Iny,node[Ilayer][Inx].Q);
                fprintf(fp2,"%-8.12f\t",node[Ilayer][Inx].Q);
            }
            printf("\n");
            fprintf(fp2,"\n");
        }
        printf("\n");
        fprintf(fp2,"\n");
    }

    fclose(fp1);
    fclose(fp2);
    fclose(fp3);

}   /* end of the program */
