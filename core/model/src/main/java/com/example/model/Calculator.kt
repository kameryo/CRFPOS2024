package com.example.model

class Calculator {
    fun calFare(adultNum: Int, childNum: Int): Int {
        return if (adultNum >= childNum) {
            adultNum * 100
        } else {
            childNum * 100
        }
    }

    fun calNormalTicketCount(adultNum: Int, childNum: Int): Int {
        return if (adultNum >= childNum) {
            adultNum * 1
        } else {
            childNum * 1
        }
    }

    fun calAccompanyTicketCount(adultNum: Int, childNum: Int): Int {
        return if (adultNum >= childNum) {
            childNum * 1
        } else {
            adultNum * 1
        }
    }

    fun calGoodsSum(selectedList: List<CartItem>?): Int {
        if (selectedList != null) {
            return selectedList.sumOf { it.goods.price * it.quantity }
        }
        return 0
    }

}
