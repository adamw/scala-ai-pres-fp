package pres

class S020_Safety_first:
  /** A Docker & dev container setup for securely running AI agents in `--dangerous` mode. All
    * container traffic is routed through a transparent mitmproxy, enforcing network access rules
    * and injecting secrets.
    */
  def Sandcat() =
    val _1 = "curl -fsSL https://raw.githubusercontent.com/VirtusLab/sandcat/master/install.sh | sh"
    val _2 = "sandcat init"
    val _3 = "sandcat run" // or reopen in devcontainer

  // echo $GITHUB_TOKEN
