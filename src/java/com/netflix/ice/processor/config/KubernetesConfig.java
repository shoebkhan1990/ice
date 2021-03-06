/*
 *
 *  Copyright 2013 Netflix, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */
package com.netflix.ice.processor.config;

import java.util.List;

public class KubernetesConfig {
	private String bucket;
	private String prefix;
	
	private List<String> clusterNameFormulae;
	private String computeTag;
	private String computeValue;
	private String namespaceTag;
	private List<KubernetesNamespaceMapping> namespaceMappings;
	private List<String> tags;
	
	public String getBucket() {
		return bucket;
	}
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public List<String> getClusterNameFormulae() {
		return clusterNameFormulae;
	}
	public void setClusterNameFormulae(List<String> clusterNameFormulae) {
		this.clusterNameFormulae = clusterNameFormulae;
	}
	public String getComputeTag() {
		return computeTag;
	}
	public void setComputeTag(String computeTag) {
		this.computeTag = computeTag;
	}
	public String getComputeValue() {
		return computeValue;
	}
	public void setComputeValue(String computeValue) {
		this.computeValue = computeValue;
	}
	public String getNamespaceTag() {
		return namespaceTag;
	}
	public void setNamespaceTag(String namespaceTag) {
		this.namespaceTag = namespaceTag;
	}
	public List<KubernetesNamespaceMapping> getNamespaceMappings() {
		return namespaceMappings;
	}
	public void setNamespaceMappings(
			List<KubernetesNamespaceMapping> namespaceMappings) {
		this.namespaceMappings = namespaceMappings;
	}
}

