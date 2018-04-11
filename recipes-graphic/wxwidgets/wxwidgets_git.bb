require wxwidgets.inc


SRC_URI = "git://github.com/wxWidgets/wxWidgets.git;protocol=git;branch=WX_3_0_BRANCH"
SRCREV = "721d62adde3f8ba8704a9cf56efeb050f652dfbf"

S = "${WORKDIR}/git"

EXTRA_OECONF = "OPENGL_LIBS='-lEGLX -ljwzgles' \
		--with-gtk=3 \
                --disable-utf8 \
                --disable-stl \
                --disable-display \
                --disable-mediactrl \
                --disable-uiactionsim \
                --disable-webview \
                --disable-gtktest \
                --disable-sdltest \
                --disable-rpath \
		--with-opengl \
		--disable-detect_sm \
		"

do_compile_append () {
    # Building the examples
    #for i in ${S}/samples ${S}/demos ; do
    #    cd $i &&  make ${PARALLEL_MAKE}
    #done

    # The wx-config file in ${S} is actully a "proxy script" so let's overrides it with the final script.
    # Isolate the path of the actual script.
    if [ -f ${S}/wx-config.old ] ; then
        wx_config_path=$(sed -n 37p ${S}/wx-config.old |sed "s/\$this_exec_prefix\///g" | cut -d \" -f 2)
    else
        wx_config_path=$(sed -n 37p ${S}/wx-config |sed "s/\$this_exec_prefix\///g" | cut -d \" -f 2)
        mv ${S}/wx-config ${S}/wx-config.old
    fi
    cp -f ${S}/$wx_config_path ${S}/wx-config
}
