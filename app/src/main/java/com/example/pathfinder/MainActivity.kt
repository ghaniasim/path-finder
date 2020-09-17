package com.example.pathfinder

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class MainActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment
    private lateinit var modelRenderable: ModelRenderable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arFragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment;
        setUpModel()
        setUpPlane()
    }

    private fun setUpPlane() {
        ModelRenderable.builder()
            .setSource(this, RenderableSource.builder().setSource(
                this,
                Uri.parse("https://github.com/KhronosGroup/glTF-Sample-Models/raw/master/2.0/Duck/glTF/Duck.gltf"),
                RenderableSource.SourceType.GLTF2)
                .setScale(0.5f)  // Scale the original model to 50%.
                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                .build())
            .setRegistryId("asim")
            .build()
            .thenAccept{modelRenderable = it}
    }

    private fun setUpModel() {
        arFragment.setOnTapArPlaneListener{ hitResult: HitResult?, plane: Plane?, motionEvent: MotionEvent? ->
            if(modelRenderable == null) {
                return@setOnTapArPlaneListener
            }
            val anchor= hitResult!!.createAnchor()
            val anchorNode= AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)
            CreateModel(anchorNode)
        }
    }

    private fun CreateModel(anchorNode: AnchorNode) {
        val viewNode= TransformableNode(arFragment.transformationSystem)
        viewNode.setParent(anchorNode)
        viewNode.renderable= modelRenderable
        viewNode.select()
    }
}