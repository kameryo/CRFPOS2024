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

    fun calGoodsSum(selectedList: List<CartItem>): Int {
        // セット商品以外の合計金額
        val normalTotal = selectedList.filter { !it.goods.isPartOfSet }
            .sumOf { it.goods.price * it.quantity }

        val setTotal = selectedList.filter { it.goods.isPartOfSet }
            .groupBy { it.goods.setId }
            .map { (_, items) ->
                val setItem = items.first()
                val setPrice = setItem.goods.setPrice ?: 0
                val setRequiredQuantity = setItem.goods.setRequiredQuantity ?: 0
                val setQuantity = items.sumOf { it.quantity }
                val setCount = setQuantity / setRequiredQuantity
                val remainder = setQuantity % setRequiredQuantity
                val setTotal = setCount * setPrice + remainder * setItem.goods.price
                setTotal
            }
            .sum()

        return normalTotal + setTotal
    }

}
