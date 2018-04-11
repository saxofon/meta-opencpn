LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=394621fd4714129e2fa0e1ed7db5d3dc"

SRC_URI = "git://github.com/vslavik/bakefile.git;protocol=git"
SRCREV = "aea9afb1b791658a205713e3e64af4517916d8af"

S = "${WORKDIR}/git"

RDEPENDS_${PN} = "python"
DEPENDS = "virtual/java-native python-native python-setuptools-native"

do_compile() {
	oe_runmake parser
}
