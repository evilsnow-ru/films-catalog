package ru.evilsnow.otus.fcatalog.event

class SimpleFavoriteRemoveController : FavoriteRemoveAware {

    private val mRemovedStore: MutableSet<Int> = HashSet()

    override fun onRemove(filmId: Int) {
        mRemovedStore.add(filmId)
    }

    override fun getRemovedFavoriteFilms(): Array<Int> =
        if (mRemovedStore.size > 0) {
            val result = Array<Int>(mRemovedStore.size) {-1}
            val it = mRemovedStore.iterator().withIndex()

            while(it.hasNext()) {
                var item = it.next()
                result[item.index] = item.value
            }

            result
        } else emptyArray()

}
