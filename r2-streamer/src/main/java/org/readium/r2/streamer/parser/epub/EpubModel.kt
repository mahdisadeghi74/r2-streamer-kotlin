/*
 * Module: r2-streamer-kotlin
 * Developers: Quentin Gliosca
 *
 * Copyright (c) 2018. Readium Foundation. All rights reserved.
 * Use of this source code is governed by a BSD-style license which is detailed in the
 * LICENSE file present in the project repository where this source code is maintained.
 */

package org.readium.r2.streamer.parser.epub

import org.readium.r2.shared.publication.Link
import org.readium.r2.shared.publication.encryption.Encryption
import java.lang.IllegalArgumentException

open class EnumCompanion<T, V>(val default: V, private val map: Map<T, V>) {
    val names: Set<T>
        get() = map.keys

    fun get(name: T) = map[name] ?: throw IllegalArgumentException("Invalid name $name")
    fun getOrNull(name: T) = map[name]
    fun getOrDefault(name: T) = map[name] ?: default
}

data class Item(
        val id: String,
        val href: String,
        val fallback: String? = null,
        val mediaOverlay: String? = null,
        val mediaType: String? = null,
        val properties: List<String> = listOf()
)

typealias Manifest = List<Item>

data class Itemref(
        val idref: String,
        val linear: Boolean,
        val properties: List<String> = listOf()
)

enum class Direction(val value: String) {
    Default("default"), Ltr("ltr"), Rtl("rtl");

    companion object : EnumCompanion<String, Direction>(
            Direction.Default, values().associateBy(Direction::value))
}

data class Spine(
        val itemrefs: List<Itemref>,
        val direction: Direction = Direction.Default,
        val toc: String? = null
)

data class PackageDocument(
        val path: String,
        val epubVersion: Double,
        val metadata: Metadata,
        val manifest: Manifest,
        val spine: Spine
)

typealias EncryptionData = Map<String, Encryption>

sealed class NavigationData

data class NavigationDocument(
        val toc: List<Link>,
        val pageList: List<Link>,
        val landmarks: List<Link>,
        val loi: List<Link>,
        val lot: List<Link>,
        val loa: List<Link>,
        val lov: List<Link>
) : NavigationData()

data class Ncx(
        val toc: List<Link>,
        val pageList: List<Link>
) : NavigationData()

data class Epub(
        val packageDocument: PackageDocument,
        val navigationData: NavigationData?,
        val encryptionData: EncryptionData?
)