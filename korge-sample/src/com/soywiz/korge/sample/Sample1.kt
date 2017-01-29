package com.soywiz.korge.sample

import com.soywiz.korge.Korge
import com.soywiz.korge.TextureResource
import com.soywiz.korge.resources.Path
import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.View
import com.soywiz.korge.view.tiles.TileSet
import com.soywiz.korge.view.tiles.tileMap
import com.soywiz.korim.bitmap.Bitmap32
import com.soywiz.korio.async.go
import com.soywiz.korio.async.sleepNextFrame
import com.soywiz.korio.async.tween.Easing
import com.soywiz.korio.async.tween.rangeTo
import com.soywiz.korio.async.tween.tween
import com.soywiz.korio.inject.AsyncInjector

object Sample1 {
	@JvmStatic fun main(args: Array<String>) = Korge(Sample1Module, args)
}

object Sample1Module : Module() {
	override val title = "Sample1"
	override val icon = "kotlin8.png"
	override var mainScene = Sample1Scene::class.java
}

class Sample1Scene(
	@Path("korge.png") val korgeTex: TextureResource,
	@Path("tiles.png") val tilesetTex: TextureResource,
	injector: AsyncInjector
) : Scene(injector) {
	suspend override fun init() {
		super.init()
		val image = views.image(korgeTex.tex, 0.5).apply {
			scale = 0.2
			rotation = Math.toRadians(-90.0)
			//smoothing = false
		}
		root += image

		val tileset = TileSet(tilesetTex.tex, 32, 32)

		val tilemap = views.tileMap(Bitmap32(8, 8), tileset).apply {
			alpha = 0.8
		}
		root += tilemap

		go {
			while (true) {
				image.alpha = if (views.root.hitTest(views.mouse) == image) 1.0 else 0.7
				sleepNextFrame()
			}
		}

		go {
			image.tween(
				View::x..200.0, View::y..200.0,
				View::rotation..Math.toRadians(0.0), View::scale..2.0,
				time = 2000, easing = Easing.EASE_IN_OUT_QUAD
			)
			for (delta in listOf(+200.0, -200.0, +100.0)) {
				image.tween(View::x..image.x + delta, time = 1000, easing = Easing.EASE_IN_OUT_QUAD)
			}
			views.dump()
		}
	}
}