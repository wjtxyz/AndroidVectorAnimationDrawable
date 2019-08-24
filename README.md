# Improve Android Frame Animation Performance
Android supply [AnimationDrawable](https://developer.android.com/reference/android/graphics/drawable/AnimationDrawable "AnimationDrawable") to display the frame animation, but which have some pitfalls, for example, AnimationDrawable always load all the children drawable at the loading time, this can take too much memory sometimes

**In this article, I demonstrate how to use the vector image to improve the frame animation performance**

## First Try
### [VectorDrawable](https://developer.android.com/reference/android/graphics/drawable/VectorDrawable "VectorDrawable")
#### How to generate
Use Adobe Illustrator or [Vector Magic](https://vectormagic.com/ "Vector Magic") to convert the raster(png/bitmap/jpg) image to vector image (svg), then import to Android Studio or use [some online tool](https://inloop.github.io/svg2android/ "some online tool") to convert the svg file to Android vector xml drawable

VectorDrawable has a big issue. [The initial loading of a vector graphic can cost more CPU cycles than the corresponding raster image. Afterward, memory use and performance are similar between the two. We recommend that you limit a vector image to a maximum of 200 x 200 dp; otherwise, it can take too long to draw.](https://developer.android.com/studio/write/vector-asset-studio#about "The initial loading of a vector graphic can cost more CPU cycles than the corresponding raster image. Afterward, memory use and performance are similar between the two. We recommend that you limit a vector image to a maximum of 200 x 200 dp; otherwise, it can take too long to draw.")


## Second Try
### [PictureDrawable](https://developer.android.com/reference/android/graphics/drawable/PictureDrawable "PictureDrawable")
#### How to generate
Use [svg-android-2](https://code.google.com/archive/p/svg-android-2/ "svg-android-2") to convert the SVG file to PictureDrawable. But to use in Android resource, we can use [Picture](https://developer.android.com/reference/android/graphics/Picture "Picture").writeToStream

|   | Loading time  | Memory  | Render CPU  |   |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| PNG Animation  | High  | High  | Low  |   |
| Vector Animation  | High  | High  | Low  |   |
| SVG Binary Animation  | Low  | Low  | High  |  |


Android Studio Profiler Snapshot
![Android Studio Profiler snapshot](https://github.com/wjtxyz/AndroidVectorFrameAnimation/blob/master/PNGvsVectorvsSVGBinary.png?raw=true "Android Studio Profiler snapshot")

Loading time comparison
![Loading time comparison](https://github.com/wjtxyz/AndroidVectorFrameAnimation/blob/master/PNGvsVectorvsSVGBinary2.png?raw=true "Loading time comparison")

Tips:
- SVG Binary not always a good option for all image, only good for computer-generated but bad for camera-generated image
- To lower the cpu & memory consumption,  you should better use the original vector image file from Adobe Illustrator or other vector editor, avoid to use the vector image file converted from raster image file
- [AnimatedVectorDrawable](https://developer.android.com/reference/android/graphics/drawable/AnimatedVectorDrawable "AnimatedVectorDrawable") is property animation not frame animation.



