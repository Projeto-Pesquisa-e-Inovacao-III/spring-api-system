{
  pkgs,
  lib,
  config,
  inputs,
  ...
}:

{
  # Provide shared libraries (libstdc++.so.6, libz.so.1) for Python C-extensions (numpy, pandas)
  env.LD_LIBRARY_PATH = lib.makeLibraryPath [
    pkgs.stdenv.cc.cc.lib
    pkgs.zlib
  ];

  packages = [
    pkgs.git
  ];

  languages.java = {
    enable = true;
    jdk.package = pkgs.jdk21;
    maven.enable = true;
  };

  languages.python = {
    enable = true;
  };

  enterShell = ''
    echo "Spring API System devenv loaded (Java $(java -version 2>&1 | head -n 1), $(python3 --version))"
  '';
}
