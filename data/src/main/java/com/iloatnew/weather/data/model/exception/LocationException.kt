package com.iloatnew.weather.data.model.exception

sealed class LocationException : Exception() {
    // Serializable object must implement 'readResolve'
    // https://www.jetbrains.com/help/inspectopedia/JavaIoSerializableObjectMustHaveReadResolve.html
    object NullLocationException : LocationException() {
        private fun readResolve(): Any = NullLocationException
    }

    object NullAddressException : LocationException() {
        private fun readResolve(): Any = NullAddressException
    }

    object LocationCanceledException : LocationException() {
        private fun readResolve(): Any = LocationCanceledException
    }
}
