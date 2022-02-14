package com.mirkamalg.domain.utils

/**
 * Created by Mirkamal Gasimov on 14.02.2022.
 */

object Regexes {
    val youtubeUrlRegex =
        """^((?:https?:)?\/\/)?((?:www|m)\.)?((?:youtube(-nocookie)?\.com|youtu.be))(\/(?:[\w\-]+\?v=|embed\/|v\/)?)([\w\-]+)(\S+)?${'$'}""".toRegex()
}