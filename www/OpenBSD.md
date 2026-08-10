Building JNA for OpenBSD
========================

This recipe was used to build the OpenBSD native library for OpenBSD. It uses
the recipe for JNA.

- fetch install ISO from https://www.openbsd.org/faq/faq4.html#Download
- use a qemu setup with:
  - a NAT network interface
  - a Hostonly interface
  - USB keyboard (PS2 does not work in install shell)
  - min. 10 GB size
  - min. 4 GB RAM
- disable DNS for hostonly:
  ```
  interface em1 {
    dns ignore;
  }
  ```
- fetch ports: https://www.openbsd.org/faq/ports/ports.html
  ```bash
  cd /tmp
  ftp https://cdn.openbsd.org/pub/OpenBSD/$(uname -r)/{ports.tar.gz,SHA256.sig}
  signify -Cp /etc/signify/openbsd-$(uname -r | cut -c 1,3)-base.pub -x SHA256.sig ports.tar.gz
  cd /usr
  tar xzf /tmp/ports.tar.gz
  rm /tmp/ports.tar.gz
  ```
- run `make` in /usr/ports/java/jna`. The resulting binary can be found in
  usr/ports/pobj/jna-BRANCH/jna-BRANCH/build/openbsd-ARCH.jar
