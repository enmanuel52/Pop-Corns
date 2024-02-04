package com.enmanuelbergling.ktormovies.data.source.preferences.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.enmanuelbergling.ktormovies.UserPref
import java.io.InputStream
import java.io.OutputStream

object UserSerializer: Serializer<UserPref> {
    override val defaultValue: UserPref
        get() = UserPref.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserPref = try {
        UserPref.parseFrom(input)
    } catch (exception: InvalidProtocolBufferException) {
        throw CorruptionException("cannot read proto", exception)
    }

    override suspend fun writeTo(t: UserPref, output: OutputStream) {
        t.writeTo(output)
    }
}