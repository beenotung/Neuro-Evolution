/*
 *Back-progapation Neural Network
 *Modified by Beeno Tung Cheung Leong
*/
#include <iostream>
#include "BackNN_train.h"
#include "BackNN_test.h"

using namespace std;

void BackNN_train(){

}

void BackNN_test(){

}

int main()
{
    cout << "Back-progapation Neural Network\nPlease select a action:\n1. Train\n2. Test" << endl;
    int choose;
    cin>>choose;
    switch (choose) {
    case 1:
        BackNN_train();
        break;
    case 2:
        BackNN_test();
        break;
    }
    return 0;
}
