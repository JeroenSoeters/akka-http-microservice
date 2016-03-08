# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure(2) do |config|
  config.vm.box = "ubuntu/trusty64"
  config.vm.network "private_network", ip: "192.168.44.23"
  config.vm.provider "virtualbox" do |vb|
    vb.memory = "4096"
  end

  config.librarian_puppet.puppetfile_dir = "puppet"
  config.librarian_puppet.placeholder_filename = ".gitkeep"

  config.vm.provider :virtualbox do |v|
    v.customize ["guestproperty", "set", :id, "/VirtualBox/GuestAdd/VBoxService/--timesync-set-threshold", 5000]
  end

  config.vm.provision "puppet" do |puppet|
    puppet.manifests_path = "puppet"
    puppet.manifest_file = "site.pp"
    puppet.module_path = "puppet/modules"
  end

  config.vm.provision "shell", inline: "mkdir -p /opt/toggles; cp /vagrant/docker/toggles/* /opt/toggles", run: "always"

end
