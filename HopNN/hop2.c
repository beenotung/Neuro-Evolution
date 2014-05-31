#include	<stdio.h>
#include	<stdlib.h>
#include	<conio.h>

#define		Ntest		2
#define		Ncycle		10
#define		Ninp		100
#define		weight_file	"num.wei"
#define		test_file	"num.tes"
#define		recall_file	"num.rec"

main()
{
	FILE *fp1, *fp2, *fp3;
	float W[Ninp][Ninp];
	float X[Ninp], X_new[Ninp];
	float sum, energy;
	int i, j;
	int Itest, Icycle;
	int check_ok;

	clrscr();

	/*---------- open files ----------*/
	fp1 = fopen(weight_file, "r");
	fp1 = fopen(test_file, "r");
	fp1 = fopen(recall_file, "w");

	if (fp1 == NULL)
	{
		puts("Weight_file not exist !!");
		exit(1);
	}
	if (fp2 == NULL)
	{
		puts("Test_file not exist !!");
		exit(1);
	}
	fseek(fp1,0,0);
	fseek(fp2,0,0);

	/*---------- input weights from weight_file ----------*/
	for(i=0;i<Ninp;i++)
		for(j=0;j<Ninp;j++)
			fscanf(fp1,"%f",&W[i][j]);

	/*---------- start recalling ----------*/
	for(Itest=0;Itest<Ntest;Itest++)
	{
		/*---------- input one test example ----------*/
		for(i=0;i<Ninp;i++)
					fscanf(fp2,"%f",&X[i]);

		for(Icycle=0;Icycle<Ncycle;Icycle++)
		{
			/*---------- iterative ----------*/
			energy=0.0;
			for(j=0;j<Ninp;j++)
			{
				sum=0.0;
				for(i=0;i<Ninp;i++)
					sum+=X[i]*W[i][j];
				if (sum<0)
					X_new[j]=-1.0;
				if (sum==0)
					X_new[j]=X[j];
				if (sum>0)
					X_new[j]=+1.0;
			}

			for(i=0;i<Ninp;i++)
				for(j=0;j<Ninp;j++)
					energy-=0.5*X[i]*W[i][j]*X[j];
			printf("Icycle= %-4d energy= %-8.2f\n",Icycle,energy);

			/*---------- check ----------*/
			check_ok=1;
			for(i=0;i<Ninp;i++)
			{
				if (X[i]!=X_new[i])
					check_ok=0;
			}
			if (check_ok==1)
				goto end_test;

			for (i=0;i<Ninp;i++)
				X[i]=X_new[i];


		}	/* end of recalling for one testing example */

		end_test:

		/*---------- write the result to recall_file ----------*/
		if (check_ok==1)
		{
			printf("\nConverge Sucess......\n");
			fprintf(fp3,"\nConverge Sucess......\n\n");
		}
		for(i=0;i<Ninp;i++)
		{
			printf("%4.0f",X[i]);
			fprintf(fp3,"%4.0f",X[i]);
		}
		printf("\n\n");
		fprintf(fp3,"\n\n");
	}	/* end of recalling for total testing examples */

	fclose(fp1);
	fclose(fp2);
	fclose(fp3);
	getch();
}	/* main */
