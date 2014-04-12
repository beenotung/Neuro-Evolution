#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define Ntest   4
#define Ninp    2
#define Nhid    2
#define Nout    1
#define weight_file "xor.wei"
#define test_file   "xor.tes"
#define recall_file "xor.rec"

main()
{
    FILE    *fp1,*fp2,*fp3;
    float   X[Ninp],T[Nout],H[Nhid],Y[Nout];
    float   W_xh[Ninp][Nhid],W_hy[Nhid][Nout];
    float   Q_h[Nhid],Q_y[Nout];
    float   sum,mse;
    int     Itest;
    int     i,j,h;

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

    /*----- input weights  from weight_file ----*/
    fseek(fp1,0,0);
    for (h=0;h<Nhid;h++)
        for (i=0;i<Ninp;i++)
            fscanf(fp1,"%f",&W_xh[i][h]);

    for (j=0;j<Nout;j++)
        for (h=0;h<Nhid;h++)
            fscanf(fp1,"%f",&W_hy[h][j]);

    for (h=0;h<Nhid;h++)
        fscanf(fp1,"%f",&Q_h[h]);

    for (j=0;j<Nout;j++)
        fscanf(fp1,"%f",&Q_y[j]);

    /*---------- Start Testing ---------*/
    fseek(fp2,0,0);
    for (Itest=0;Itest<Ntest;Itest++)
    {
        /*..... input one testing example ....*/
        for (i=0;i<Ninp;i++)
            fscanf(fp2,"%f",&X[i]);
        for (j=0;j<Nout;j++)
            fscanf(fp2,"%f",&T[j]);

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

        /*..... compute the mean_square_error ....*/
        mse=0.0;
        for (j=0;j<Nout;j++)
            mse+=(T[j]-Y[j])*(T[j]-Y[j]);

        /*---- Write the results to recall_file -----*/
        printf("T[j]= ");
        fprintf(fp3,"T[j]= ");
        for (j=0;j<Nout;j++)
        {
            printf("%-8.2f\t",T[j]);
            fprintf(fp3,"%-8.2f\t",T[j]);
        }
        printf("Y[j]= ");
        fprintf(fp3,"Y[j]= ");
        for (j=0;j<Nout;j++)
        {
            printf("%-8.2f\t",Y[j]);
            fprintf(fp3,"%-8.2f\t",Y[j]);
        }
        printf("  mse= %-8.4f\n\n",mse);
        fprintf(fp3,"  mse= %-8.4f\n\n",mse);
        fprintf(fp3,"\n");
    }   /* end of recalling for total test example */

    fclose(fp1);
    fclose(fp2);
    fclose(fp3);

}   /* end of the program */

