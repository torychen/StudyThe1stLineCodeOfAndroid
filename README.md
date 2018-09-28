# StudyThe1stLineCodeOfAndroid
My code of the book "The first line code of Android"

At office, use TortoiseGit, need to generate PuTTYgen required key as the following steps:

+ generate ssh key

    + mkdir ~/.ssh when it does NOT exist

    + ssh-keygen -t rsa -b 4096 -C "your email"

+ copy public key in **id_rsa.pub** to git hub

+  ssh -T git@github.com

+ when use TortoiseGit

    + puttygen, load  id_rsa
        
    + Pageant, add key by the file just generated.   

