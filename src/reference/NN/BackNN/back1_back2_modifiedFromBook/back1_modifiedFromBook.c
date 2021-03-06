#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define Ncycle  1000
#define Ntrain  4
#define Ninp    2
#define Nhid    2
#define Nout    1
#define eta     0.5
#define alpha   0.5
#define train_file  "xor.tra"
#define weight_file "xor.wei"
#define mse_file    "xor.mse"

float random_value(void);

main()
{
    FILE    *fp1,*fp2,*fp3;
    float   X[Ninp],T[Nout],H[Nhid],Y[Nout];
    float   W_xh[Ninp][Nhid],W_hy[Nhid][Nout];
    float   dW_xh[Ninp][Nhid],dW_hy[Nhid][Nout];
    float   Q_h[Nhid],Q_y[Nout];
    float   dQ_h[Nhid],dQ_y[Nout];
    float   delta_h[Nhid],delta_y[Nout];
    float   sum,mse;
    int     Icycle,Itrain;
    int     i,j,h;
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

    /*----- initialize weights ---*/
    srand(time(&now)%1000);
    for (h=0;h<Nhid;h++)
        for (i=0;i<Ninp;i++)
        {
            W_xh[i][h]=random_value();
            dW_xh[i][h]=0;
        }
    for (j=0;j<Nout;j++)
        for (h=0;h<Nhid;h++)
        {
            W_hy[h][j]=random_value();
            dW_hy[h][j]=0;
        }
    for (h=0;h<Nhid;h++)
    {
        Q_h[h]=0;
        dQ_h[h]=0;
        delta_h[h]=0;
    }
    for (j=0;j<Nout;j++)
    {
        Q_y[j]=random_value();
        dQ_y[j]=0;
        delta_y[j]=0;
    }

    /*-------- Start Learning ---------*/
    for (Icycle=0;Icycle<Ncycle;Icycle++)
    {
        mse=0.0;
        /*.. input one training example ...*/
        fseek(fp1,0,0);
        for (Itrain=0;Itrain<Ntrain;Itrain++)
        {
            for (i=0;i<Ninp;i++)
                fscanf(fp1,"%f",&X[i]);
            for (j=0;j<Nout;j++)
                fscanf(fp1,"%f",&T[j]);

            /*..... compute H,Y .....*/
            for (h=0;h<Nhid;h++)
            {
                sum=0.0;
                for (i=0;i<Ninp;i++)
                    sum+=X[i]*W_xh[i][h];
                H[h]=(float)1.0/(1.0+exp(-(sum-Q_h[h])));
            }
            for (j=0;j<Nout;j++)
            {
                sum=0.0;
                for (h=0;h<Nhid;h++)
                    sum+=H[h]*W_hy[h][j];
                Y[j]=(float)1.0/(1.0+exp(-(sum-Q_y[j])));
            }

            /*..... compute delta .....*/
            for (j=0;j<Nout;j++)
                delta_y[j]=Y[j]*(1-Y[j])*(T[j]-Y[j]);

            for (h=0;h<Nhid;h++)
            {
                sum=0.0;
                for (j=0;j<Nout;j++)
                    sum+=W_hy[h][j]*delta_y[j];
                delta_h[h]=H[h]*(1-H[h])*sum;
            }

            /*..... compute dW,dQ .....*/
            for (j=0;j<Nout;j++)
                for (h=0;h<Nhid;h++)
                    dW_hy[h][j]=eta*delta_y[j]*H[h]+alpha*dW_hy[h][j];

            for (j=0;j<Nout;j++)
                dQ_y[j]=-eta*delta_y[j]+alpha*dQ_y[j];

            for (h=0;h<Nhid;h++)
                for (i=0;i<Ninp;i++)
                    dW_xh[i][h]=eta*delta_h[h]*X[i]+alpha*dW_xh[i][h];

            for (h=0;h<Nhid;h++)
                dQ_h[h]=-eta*delta_h[h]+alpha*dQ_h[h];

            /*..... compute new W,Q .....*/
            for (j=0;j<Nout;j++)
                for (h=0;h<Nhid;h++)
                    W_hy[h][j]+=dW_hy[h][j];

            for (j=0;j<Nout;j++)
                Q_y[j]+=dQ_y[j];

            for (h=0;h<Nhid;h++)
                for (i=0;i<Ninp;i++)
                    W_xh[i][h]+=dW_xh[i][h];

            for (h=0;h<Nhid;h++)
                Q_h[h]+=dQ_h[h];

            /*... compute the mean_square_error ...*/
            for (j=0;j<Nout;j++)
                mse+=(T[j]-Y[j])*(T[j]-Y[j]);

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
    for (h=0;h<Nhid;h++)
    {
        for (i=0;i<Ninp;i++)
        {
            printf("W_xh[%2d][%2d]=%-8.12f\t",i,h,W_xh[i][h]);
            fprintf(fp2,"%-8.12f\t",W_xh[i][h]);
        }
        printf("\n");
        fprintf(fp2,"\n");
    }
    printf("\n");
    fprintf(fp2,"\n");
    for (j=0;j<Nout;j++)
    {
        for (h=0;h<Nhid;h++)
        {
            printf("W_hy[%2d][%2d]=%-8.12f\t",h,j,W_hy[h][j]);
            fprintf(fp2,"%-8.12f\t",W_hy[h][j]);
        }
        printf("\n");
        fprintf(fp2,"\n");
    }
    printf("\n");
    fprintf(fp2,"\n");
    for (h=0;h<Nhid;h++)
    {
        printf("Q_h[%2d]=%-8.12f\t",h,Q_h[h]);
        fprintf(fp2,"%-8.12f\t",Q_h[h]);
    }
    printf("\n\n");
    fprintf(fp2,"\n\n");
    for (j=0;j<Nout;j++)
    {
        printf("Q_y[%2d]=%-8.12f\t",j,Q_y[j]);
        fprintf(fp2,"%-8.12f\t",Q_y[j]);
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
