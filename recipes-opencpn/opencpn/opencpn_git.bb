DESCRIPTION = "OpenCPN is a free software (GPLv2) project to create a concise chartplotter and navigation software for use as an underway or planning tool. OpenCPN is developed by a team of active sailors using real world conditions for program testing and refinement."
HOMEPAGE = "http://opencpn.org/ocpn/"
LICENSE = "GPLv2+"

LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
PR = "r0"

SRC_URI = "git://github.com/OpenCPN/OpenCPN.git;protocol=git"
SRCREV = "bcbda2aa222df52857dc9a179c2492be5acd0222"

inherit cmake

S = "${WORKDIR}/git"

DEPENDS = "mesa libtinyxml wxwidgets gtk+3 libxcalibrate gpsd eglx"
RDEPENDS_${PN} = "mesa libtinyxml wxwidgets gtk+3 libxcalibrate gpsd eglx"

EXTRA_OECMAKE = "-DCMAKE_SKIP_RPATH=ON \
                 -DCMAKE_INSTALL_PREFIX=${prefix} \
                 -DCFLAGS='-ggdb -march=native' \
		 -DSKIP_PLUGINS=ON \
                 "

do_configure_prepend () {
	ln -sf ${WORKDIR}/FindGTK3.cmake ${S}
}

FILES_${PN} = "${datadir}/* ${bindir}/opencpn"
