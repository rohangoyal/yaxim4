//////////////////////////////////////////////////////////////////////////////
// 
//  Copyright (c) 2011 Aquevix Solutions Pvt Ltd. All rights reserved.
// 
//  Redistribution and use in source and binary forms, with or without
//  modification, are permitted provided that the following conditions are
//  met:
// 
//     * Redistributions of source code must retain the above copyright
//       notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above
//       copyright notice, this list of conditions and the following disclaimer
//       in the documentation and/or other materials provided with the
//       distribution.
//     * Neither the name of Aquevix Solutions Pvt Ltd. nor the names of its
//       contributors may be used to endorse or promote products derived from
//       this software without specific prior written permission.
//     * Aquevix reserves the right to redistribute the source code, binaries,
//       derivative works, techniques, documents, designs and other techniques
//       used in this framework under a different license.
// 
//     For any questions you may contact us at:
// 
//       Attn:
//         Aquevix Solutions Pvt Ltd.
//         Suite 8-D, A-8 Bigjo's Tower,
//         Netaji Subhash Place,
//         New Delhi - 110034, INDIA
// 
//       Contact:
//         http://www.aquevix.com
//         info@aquevix.com
//         +91-11-45600412
// 
//  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
//  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
//  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
//  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
//  OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
//  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
//  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
//  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
//  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
//  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//////////////////////////////////////////////////////////////////////////////
package com.namonamo.app.service;

public class CachedDataHook extends DataHook {

	String     _idColumn = "id";
    String     _tsColumn = "ts";
    
    
	@Override
	void onBeforeExecute(Request request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void onAfterExecute(Response response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void save(Response response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	String load(Request request) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
