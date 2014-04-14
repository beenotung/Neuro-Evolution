#ifndef utility.h   /*only define this header once*/

#define utility.h
#include <stdlib.h>
float random_value(void)
{
    //return(((double)rand()/52767.0-0.1));
    return(((double)rand()/RAND_MAX));
}

#endif      /*end of this header*/
