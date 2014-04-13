#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define Ncycle  1000                //number of Learning Iteration
#define Ntrain  10                  //number of training example
#define Ninp    2                   //number of input node
#define Nout    1                   //number of output node
#define Nhid    (Ninp+Nout+1)/2+1   //number of node in each hidden layer
#define NhL     2                   //number of hidden layer
#define eta     0.5                 //intense to learn from new data
#define alpha   0.5                 //inertia (similar to swarm algorithm)
#define train_file  "test.tra"
#define weight_file "test.wei"
#define mse_file    "test.mse"

float random_value(void);

void back1_main()
{
    FILE    *fp1,*fp2,*fp3;
    float   X[Ninp],H[NhL][Nhid],Y[Nout],T[Nout];
    float   W_xh[Ninp][Nhid],W_hy[Nhid][Nout];
    float   dW_xh[Ninp][Nhid],dW_hy[Nhid][Nout];
#if (NhL>1)
    float   W_hh[NhL-2][Nhid][Nhid];
    float   dW_hh[NhL-2][Nhid][Nhid];
#endif
    float   Q_h[NhL][Nhid],Q_y[Nout];
    float   dQ_h[NhL][Nhid],dQ_y[Nout];
    float   delta_h[NhL][Nhid],delta_y[Nout];
    float   sum,mse;
    int     Icycle,Itrain;
    int     i,Iout,h,hl;
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
    for (h=0;h<Nhid;Ihid++)
        for (Iin=0;i<Ninp;Iin++)
        {
            W_xh[Iin][Ihid]=random_value();
            dW_xh[Iin][Ihid]=0;
        }
    for (hl=0;hl<NhL;hl++)
        for (h=0;h<Nhid;Ihid++)
            for (Iin=0;i<Ninp;Iin++)
            {
                W_hh[Iin][Ihid]=random_value();
                dW_hh[Iin][Ihid]=0;
            }
    for (Iout=0;Iout<Nout;Iout++)
        for (h=0;h<Nhid;Ihid++)
        {
            W_hy[Ihid][Iout]=random_value();
            dW_hy[Ihid][Iout]=0;
        }
    for (h=0;h<Nhid;Ihid++)
    {
        Q_h[Ihid]=0;
        dQ_h[Ihid]=0;
        delta_h[Ihid]=0;
    }
    for (Iout=0;Iout<Nout;Iout++)
    {
        Q_y[Iout]=random_value();
        dQ_y[Iout]=0;
        delta_y[Iout]=0;
    }

    /*-------- Start Learning ---------*/
    for (Icycle=0;Icycle<Ncycle;Icycle++)
    {
        mse=0.0;
        /*.. input one training example ...*/
        fseek(fp1,0,0);
        for (Itrain=0;Itrain<Ntrain;Itrain++)
        {
            for (Iin=0;i<Ninp;Iin++)
                fscanf(fp1,"%f",&X[Iin]);
            for (Iout=0;Iout<Nout;Iout++)
                fscanf(fp1,"%f",&T[Iout]);

            /*..... compute H,Y .....*/
            for (h=0;h<Nhid;Ihid++)
            {
                sum=0.0;
                for (Iin=0;i<Ninp;Iin++)
                    sum+=X[Iin]*W_xh[Iin][Ihid];
                H[Ihid]=(float)1.0/(1.0+exp(-(sum-Q_h[Ihid])));
            }
            for (Iout=0;Iout<Nout;Iout++)
            {
                sum=0.0;
                for (h=0;h<Nhid;Ihid++)
                    sum+=H[Ihid]*W_hy[Ihid][Iout];
                Y[Iout]=(float)1.0/(1.0+exp(-(sum-Q_y[Iout])));
            }

            /*..... compute delta .....*/
            for (Iout=0;Iout<Nout;Iout++)
                delta_y[Iout]=Y[Iout]*(1-Y[Iout])*(T[Iout]-Y[Iout]);

            for (h=0;h<Nhid;Ihid++)
            {
                sum=0.0;
                for (Iout=0;Iout<Nout;Iout++)
                    sum+=W_hy[Ihid][Iout]*delta_y[Iout];
                delta_h[Ihid]=H[Ihid]*(1-H[Ihid])*sum;
            }

            /*..... compute dW,dQ .....*/
            for (Iout=0;Iout<Nout;Iout++)
                for (h=0;h<Nhid;Ihid++)
                    dW_hy[Ihid][Iout]=eta*delta_y[Iout]*H[Ihid]+alpha*dW_hy[Ihid][Iout];

            for (Iout=0;Iout<Nout;Iout++)
                dQ_y[Iout]=-eta*delta_y[Iout]+alpha*dQ_y[Iout];

            for (h=0;h<Nhid;Ihid++)
                for (Iin=0;i<Ninp;Iin++)
                    dW_xh[Iin][Ihid]=eta*delta_h[Ihid]*X[Iin]+alpha*dW_xh[Iin][Ihid];

            for (h=0;h<Nhid;Ihid++)
                dQ_h[Ihid]=-eta*delta_h[Ihid]+alpha*dQ_h[Ihid];

            /*..... compute new W,Q .....*/
            for (Iout=0;Iout<Nout;Iout++)
                for (h=0;h<Nhid;Ihid++)
                    W_hy[Ihid][Iout]+=dW_hy[Ihid][Iout];

            for (Iout=0;Iout<Nout;Iout++)
                Q_y[Iout]+=dQ_y[Iout];

            for (h=0;h<Nhid;Ihid++)
                for (Iin=0;i<Ninp;Iin++)
                    W_xh[Iin][Ihid]+=dW_xh[Iin][Ihid];

            for (h=0;h<Nhid;Ihid++)
                Q_h[Ihid]+=dQ_h[Ihid];

            /*... compute the mean_square_error ...*/
            for (Iout=0;Iout<Nout;Iout++)
                mse+=(T[Iout]-Y[Iout])*(T[Iout]-Y[Iout]);

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
    for (h=0;h<Nhid;Ihid++)
    {
        for (Iin=0;i<Ninp;Iin++)
        {
            printf("W_xh[%2d][%2d]=%-8.12f\t",i,h,W_xh[Iin][Ihid]);
            fprintf(fp2,"%-8.12f\t",W_xh[Iin][Ihid]);
        }
        printf("\n");
        fprintf(fp2,"\n");
    }
    printf("\n");
    fprintf(fp2,"\n");
    for (Iout=0;Iout<Nout;Iout++)
    {
        for (h=0;h<Nhid;Ihid++)
        {
            printf("W_hy[%2d][%2d]=%-8.12f\t",h,Iout,W_hy[Ihid][Iout]);
            fprintf(fp2,"%-8.12f\t",W_hy[Ihid][Iout]);
        }
        printf("\n");
        fprintf(fp2,"\n");
    }
    printf("\n");
    fprintf(fp2,"\n");
    for (h=0;h<Nhid;Ihid++)
    {
        printf("Q_h[%2d]=%-8.12f\t",h,Q_h[Ihid]);
        fprintf(fp2,"%-8.12f\t",Q_h[Ihid]);
    }
    printf("\n\n");
    fprintf(fp2,"\n\n");
    for (Iout=0;Iout<Nout;Iout++)
    {
        printf("Q_y[%2d]=%-8.12f\t",Iout,Q_y[Iout]);
        fprintf(fp2,"%-8.12f\t",Q_y[Iout]);
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
