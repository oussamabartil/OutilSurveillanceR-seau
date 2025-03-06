# Développement d’une application de détection des intrusions réseau

## Description du Projet

Ce projet consiste à développer une application JavaFX permettant de surveiller le trafic réseau et de détecter les activités suspectes en temps réel. L'objectif principal est de fournir aux administrateurs système un outil puissant et intuitif pour identifier les comportements malveillants et protéger les infrastructures réseau. L'application sera capable de détecter des attaques par déni de service (DoS), des balayages de ports, des tentatives de piratage, et autres types d'intrusions.

## Fonctionnalités

L'application offre plusieurs fonctionnalités clés pour analyser le trafic réseau en temps réel et détecter les intrusions :

- **Surveillance du Trafic Réseau :** Capture et analyse des paquets réseau entrants et sortants en temps réel.
- **Détection d'Intrusions :** Détection des activités malveillantes telles que les attaques DoS, les balayages de ports, etc.
- **Alertes en Temps Réel :** Système de notification pour alerter les administrateurs lors de la détection d'intrusions.
- **Interface Utilisateur Interactive :** Interface graphique développée avec JavaFX pour afficher les informations critiques.
- **Rapports Détailés :** Génération de rapports sur les incidents de sécurité et recommandations pour améliorer la sécurité.

### Détails de l'Interface Utilisateur (JavaFX)

L'interface graphique permettra de visualiser des informations essentielles telles que :

1. **Connexions Actives :** Nombre de connexions actives sur le réseau.
2. **Paquets Réseau :** Affichage des paquets entrants et sortants par adresse IP.
3. **Alertes de Sécurité :** Affichage des alertes en cas d’intrusion détectée.
4. **Statistiques Globales :** Graphiques et statistiques détaillées sur le trafic réseau (nombre de paquets, types de protocoles, etc.).

### Technologies Utilisées

- **JavaFX :** Pour le développement de l'interface graphique.
- **Pcap4J :** Bibliothèque Java pour la capture et l'analyse du trafic réseau.
- **Heuristiques et Apprentissage Automatique :** Algorithmes utilisés pour détecter les comportements suspects et malveillants.
- **Système de Notifications :** Pour informer les administrateurs en temps réel.

## Installation et Prérequis

### Prérequis
Avant de pouvoir utiliser l'application, assurez-vous d'avoir les outils suivants installés sur votre machine :

- **Java 11 ou version ultérieure** (JDK)
- **Apache Maven** pour la gestion des dépendances.
- **Pcap4J** pour l'analyse du trafic réseau.

