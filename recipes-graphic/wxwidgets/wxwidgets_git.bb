inherit binconfig autotools pkgconfig

SUMMARY = "The GTK+ 3 port of the wxWidgets library"
DESCRIPTION = "wxBase is a collection of C++ classes providing basic data structures (strings, \
lists, arrays), portable wrappers around many OS-specific funstions (file \
operations, time/date manipulations, threads, processes, sockets, shared \
library loading) as well as other utility classes (streams, archive and \
compression). wxBase currently supports Win32, most Unix variants (Linux, \
FreeBSD, Solaris, HP-UX) and MacOS X (Carbon and Mach-0)."
SECTION = "graphic"
LICENSE = "wxWindows"
LIC_FILES_CHKSUM = "file://docs/licence.txt;md5=18346072db6eb834b6edbd2cdc4f109b"

SRC_URI = "gitsm://github.com/wxWidgets/wxWidgets.git;protocol=git;branch=WX_3_0_BRANCH"
SRCREV = "721d62adde3f8ba8704a9cf56efeb050f652dfbf"

RDEPENDS_${PN} = "gtk+3"
DEPENDS = "cppunit tiff libsdl gtk+3 autoconf-archive libglu bakefile"

PACKAGES = "${PN} ${PN}-dev ${PN}-dbg"

#PACKAGECONFIG = "${@base_contains('DISTRO_FEATURES', 'x11', 'x11', '', d)}"
##PACKAGECONFIG = "${@base_contains('DISTRO_FEATURES', 'egl', 'egl', '', d)}"
#PACKAGECONFIG = "${@base_contains('DISTRO_FEATURES', 'wayland', 'wayland', '', d)}"
PACKAGECONFIG[x11] = "--with-x, --with-x=no --disable-stc, "
#PACKAGECONFIG[egl] = "--with-opengl, --with-opengl=no, virtual/egl"
PACKAGECONFIG[wayland] = "--with-x=no --disable-stc, , eglx"

S = "${WORKDIR}/git"

EXTRA_OECONF = "OPENGL_LIBS='-lEGLX -ljwzgles' \
		--with-gtk=3 \
		--with-opengl \
		--with-libtiff=sys \
                --disable-utf8 \
                --disable-stl \
                --disable-display \
                --disable-mediactrl \
                --disable-uiactionsim \
                --disable-webview \
                --disable-gtktest \
                --disable-sdltest \
                --disable-rpath \
		--disable-detect_sm \
		"

# We're obliged to overrides this because any call to autoheader
# will result in "heavy failures".
autotools_do_configure () {
    cd ${B}
#    ./autogen.sh

    if [ -e ${S}/configure ]; then
        cd ${B}
        oe_runconf
    else
        bbnote no configure
    fi
}

do_compile_append () {
    :
    # Building the examples
    #for i in ${S}/samples ${S}/demos ; do
    #    cd $i &&  make ${PARALLEL_MAKE}
    #done
}


do_install_append () {
    for i in $(find ${B}/demos/ ${B}/samples/ -type f -executable | egrep -v "\.[^/]+$") ; do
       cp $i ${D}${bindir}
    done

    # The wx-config file in ${S} is actually a "proxy script" so let's overrides it with the final script.
    # Isolate the path of the actual script.
    if [ -f ${B}/wx-config.old ] ; then
       wx_config_path=$(sed -n 37p ${B}/wx-config.old |sed "s/\$this_exec_prefix\///g" | cut -d \" -f 2)
    else
       wx_config_path=$(sed -n 37p ${B}/wx-config |sed "s/\$this_exec_prefix\///g" | cut -d \" -f 2)
       mv ${B}/wx-config ${B}/wx-config.old
    fi
    rm ${D}/${bindir}/wx-config
    cp -f ${B}/$wx_config_path ${D}/${bindir}/wx-config
}

INSANE_SKIP_${PN} = "dev-so"

FILES_${PN} = "${bindir}/* ${libdir}/wx/* ${libdir}/libwx*"
FILES_${PN}-dev = " ${datadir}/bakefile/* ${datadir}/aclocal/* ${prefix}/src/* ${includedir}/*"
FILES_${PN}-dbg = "${libdir}/.debug/libwx* ${bindir}/.debug/*"
