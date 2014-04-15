#ifndef utility.h   /*only define this header once*/

#define utility.h
#include <stdlib.h>

float random_value(void)
{
    //return(((double)rand()/52767.0-0.1));
    return(((double)rand()/RAND_MAX));
}

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

#endif      /*end of this header*/
