## Inhaltsverzeichnis

- [GitLab CI](#gitlab%20ci)
- [Kubernetes integration](#kubernetes%20integration)

## GitLab CI

You can use GitLab-CI to test your project. You can find more information and examples to get started on [CI](https://reset.inso.tuwien.ac.at/repo/pub/instructions/-/wikis/CI).

To provide your own runner (which you normally do not need to do) do the following:

* Install GitLab Runner
* Specify the following URL during the Runner setup: <https://reset.inso.tuwien.ac.at/repo/>
* Use the following registration token during setup: GR1348941kcqTE82vvccR5m-oqdND
* Start the Runner!

## Kubernetes integration

Continuous Delivery (SIMPLE variant) using Kubernetes is configured for your project. You can find more information at [k8s](https://reset.inso.tuwien.ac.at/repo/pub/instructions/-/wikis/k8s-SIMPLE).

Your host name for the deployed ingress is <https://22ss-sepm-pr-qse-14-fhdytszwrmnecfwbkvm8p.apps.student.inso-w.at>