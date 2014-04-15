#include <stdio.h>
#include <stdlib.h>
#include <vector>
#include <math.h>
using namespace std;

#define Ncycle  1000                //number of Learning Iteration
#define Ntrain  10                  //number of training example
#define Ninp    2                   //number of input node
#define Nout    1                   //number of output node
#define Nhid    (Ninp+Nout+1)/2+1   //number of node in each hidden layer
#define Nhl     1                   //number of hidden layer
#define Nlayer  1+Nhl+1             //number of layer
#define eta     0.5                 //intense to learn from new data
#define alpha   0.5                 //inertia (similar to swarm algorithm)
#define train_file  "test.tra"
#define weight_file "test.wei"
#define mse_file    "test.mse"

float random_value(void);

struct nodetype
{
    vector<float> W,dW;
    float Q,dQ,delta,Output;
};

int Lsize(int i)
{
    int size;
    switch (i)
    {
    case 0:
        size=Ninp;
        break;
    case (Nlayer-1):
        size=Nout;
        break;
    default:
        size=Nhid;
    }
    return size;
}

void BackNN_train()
{
    FILE    *fp1,*fp2,*fp3;
    vector<nodetype> node[Nlayer];      //node[layer in the NN][node in a layer]
    float   T[Nout];                    //Training target output (sample)
    float   sum,mse;
    int     Icycle,Itrain;
    int     Ilayer,Inx,Iny;             //Inodex and Inodey
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
        for (Iny=0; Iny<Lsize(Ilayer); Iny++)
        {
            node[Ilayer].clear();
            nodetype dnode;
            dnode.W.clear();
            dnode.dW.clear();
            if (Ilayer!=0)
                for (Inx=0; Inx<Lsize(Ilayer-1); Inx++)
                {
                    dnode.W.push_back(random_value());
                    dnode.dW.push_back(0);
                }
            dnode.Q=0;          //??//random for the last one?
            dnode.dQ=0;
            dnode.delta=0;
            node[Ilayer].push_back(dnode);
        }
    }

    /*-------- Start Learning ---------*/
    for (Icycle=0; Icycle<Ncycle; Icycle++)
    {
        mse=0.0;
        /*.. input one training example ...*/
        fseek(fp1,0,0);
        for (Itrain=0; Itrain<Ntrain; Itrain++)
        {
            for (Inx=0; Inx<Ninp; Inx++)
                fscanf(fp1,"%f",&node[0][Inx].Output);
            for (Iny=0; Iny<Nout; Iny++)
                fscanf(fp1,"%f",&T[Iny]);

            /*..... compute Output .....*/
            for (Ilayer=1; Ilayer<Nlayer; Ilayer++)
            {
                sum=0;
                for (Iny=0; (unsigned)Iny<node[Ilayer-1].size(); Iny++)
                {
                    for (Inx=0; (unsigned)Inx<node[Ilayer-1].size(); Inx++)
                        sum+=node[Ilayer-1][Inx].Output*node[Ilayer][Iny].W[Inx];
                    node[Ilayer][Iny].Output=(float)1.0/(1.0+exp(-(sum-node[Ilayer][Iny].Q)));
                }
            }

            /*..... compute delta .....*/
                for (Iny=0; (unsigned)Iny<node[Ilayer-1].size(); Iny++)
                        node[Ilayer-1].[Iny].delta=


            for (Ilayer=Nlayer-1;Ilayer>-1;Ilayer--)




                        /// editing up to here
            for (Iy=0; Iy<Nout; Iy++)
                delta_y[Iy]=Y[Iy]*(1-Y[Iy])*(T[Iy]-Y[Iy]);

            for (Ihx=0; Ihx<Nhid; Ihx++)
            {
                sum=0.0;
                for (Iy=0; Iy<Nout; Iy++)
                    sum+=W_hy[Ihx][Iy]*delta_y[Iy];
                delta_h[Nhl-1][Ihx]=H[Ihx]*(1-H[Ihx])*sum;
            }
            for (Ihl=Nhl-1; Ihl>-1; Ihl--)
                for (Ihx=0; Ihx<Nhid; Ihx++)
                {
                    sum=0.0;
                    for (Ihy=0; Ihy<Nhid; Ihy++)
                        sum+=W_hh[Ihx][Ihy]*delta_y[Iy];//
                    delta_h[Ihx]=H[Ihx]*(1-H[Ihx])*sum;
                }

            /*..... compute dW,dQ .....*/
            for (Iy=0; Iy<Nout; Iy++)
                for (Ihid=0; Ihid<Nhid; Ihid++)
                    dW_hy[Ihid][Iy]=eta*delta_y[Iy]*H[Ihid]+alpha*dW_hy[Ihid][Iy];

            for (Iy=0; Iy<Nout; Iy++)
                dQ_y[Iy]=-eta*delta_y[Iy]+alpha*dQ_y[Iy];

            for (Ihid=0; Ihid<Nhid; Ihid++)
                for (Ix=0; Ix<Ninp; Ix++)
                    dW_xh[Ix][Ihid]=eta*delta_h[Ihid]*X[Ix]+alpha*dW_xh[Ix][Ihid];

            for (Ihid=0; Ihid<Nhid; Ihid++)
                dQ_h[Ihid]=-eta*delta_h[Ihid]+alpha*dQ_h[Ihid];

            /*..... compute new W,Q .....*/
            for (Iy=0; Iy<Nout; Iy++)
                for (Ihid=0; Ihid<Nhid; Ihid++)
                    W_hy[Ihid][Iy]+=dW_hy[Ihid][Iy];

            for (Iy=0; Iy<Nout; Iy++)
                Q_y[Iy]+=dQ_y[Iy];

            for (Ihid=0; Ihid<Nhid; Ihid++)
                for (Ix=0; Ix<Ninp; Ix++)
                    W_xh[Ix][Ihid]+=dW_xh[Ix][Ihid];

            for (Ihid=0; Ihid<Nhid; Ihid++)
                Q_h[Ihid]+=dQ_h[Ihid];

            /*... compute the mean_square_error ...*/
            for (Iy=0; Iy<Nout; Iy++)
                mse+=(T[Iy]-Y[Iy])*(T[Iy]-Y[Iy]);

        }   /* end of 1 learning cycle */

        /*... write the mse_value to mse_file ...*/
        mse/=Ntrain;
        if ((Icycle % 10)==9)
        {
            printf("\nIcycle=%3d mse=%-8.12f\n",Icycle,mse);
            fprintf(fp3,"%3d %-8.12f\n",Icycle,mse);
        }

    }   /* end of total learning cycle */

    /*---- Write the weights to weight_file -----*/
    printf("\n");
    for (Ihid=0; Ihid<Nhid; Ihid++)
    {
        for (Ix=0; Ix<Ninp; Ix++)
        {
            printf("W_xh[%2d][%2d]=%-8.12f\t",i,h,W_xh[Ix][Ihid]);
            fprintf(fp2,"%-8.12f\t",W_xh[Ix][Ihid]);
        }
        printf("\n");
        fprintf(fp2,"\n");
    }
    printf("\n");
    fprintf(fp2,"\n");
    for (Iy=0; Iy<Nout; Iy++)
    {
        for (Ihid=0; Ihid<Nhid; Ihid++)
        {
            printf("W_hy[%2d][%2d]=%-8.12f\t",h,Iy,W_hy[Ihid][Iy]);
            fprintf(fp2,"%-8.12f\t",W_hy[Ihid][Iy]);
        }
        printf("\n");
        fprintf(fp2,"\n");
    }
    printf("\n");
    fprintf(fp2,"\n");
    for (Ihid=0; Ihid<Nhid; Ihid++)
    {
        printf("Q_h[%2d]=%-8.12f\t",h,Q_h[Ihid]);
        fprintf(fp2,"%-8.12f\t",Q_h[Ihid]);
    }
    printf("\n\n");
    fprintf(fp2,"\n\n");
    for (Iy=0; Iy<Nout; Iy++)
    {
        printf("Q_y[%2d]=%-8.12f\t",Iy,Q_y[Iy]);
        fprintf(fp2,"%-8.12f\t",Q_y[Iy]);
    }
    printf("\n\n");
    fprintf(fp2,"\n\n");

    fclose(fp1);
    fclose(fp2);
    fclose(fp3);

}   /* end of the program */

float random_value(void)
{
    //return(((double)rand()/52767.0-0.1));
    return(((double)rand()/RAND_MAX));
}
