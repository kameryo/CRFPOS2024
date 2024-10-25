package com.example.model

class Calculator {
    fun calFare(adultNum: Int, childNum: Int): Int {
        return if (adultNum >= childNum) {
            adultNum * 100
        } else {
            childNum * 100
        }
    }

//    fun calSubTotalPendingPurchase(selectedList: List<PendingPurchase>?): Int {
//        if (selectedList != null) {
//            return selectedList.sumOf { it.amount }
//        }
//        return 0
//    }

}
