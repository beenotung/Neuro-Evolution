#include	<stdio.h>
#include	<stdlib.h>
#include	<conio.h>

#define		Ntrain		10
#define		Ninp		100
#define		train_file	"num.tra"
#define		weight_file	"num.wei"

main ()
{

	FILE	*fp1,*fp2;
	float	W[Ninp][Ninp];
	float	X[Ninp];
	int		Itrain;
	int		i,j;

	clrscr();

	/*---------- open files ----------*/
	fp1=fopne(train_file,"r");
	fp2=fopen(weight_file,"w");
	if (fp1==NULL)
	{
		puts("File not exist !!");
		exit(1);
	}
	fseek(fp1,0,0);

	/*---------- initialize weights ----------*/
	for(i=0;i<Ninp;i++)
	{
		for(j=0;j<Ninp;j++)
			W[i][j]=0.0;
	}

	/*---------- start learning ----------*/
	for(Itrain=0;Itrain<Ntrain;Itrain++)
	{
		/*---------- input one training example ----------*/
		for(i=0;i<Ninp;i++)
			fscanf(fp1,"%f",&X[i]);

		for(i=0;i<Ninp;i++)
			for(j=0;j<Ninp;j++)
				W[i][j]+=X[i]*X[j];
		/* end of learning for one train example */

	}	/* end of learning for total train examples */

	for(i=0;i<Ninp;i++)
		W[i][i]=0.0;

	/*---------- write the weights to weight file ----------*/
	for(i=0;i<Ninp;i++)
	{
		for(j=0;j<Ninp;j++)
		{
			printf("%-8.2f",W[i][j]);
			fprintf(fp2,"%-8.2f",W[i][j]);
		}
		printf("\n");
		printf(fp2,"\n");
	}

	fclose(fp1);
	fclose(fp2);
	getch();
}	/* main */
